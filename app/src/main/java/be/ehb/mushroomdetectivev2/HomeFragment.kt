package be.ehb.mushroomdetectivev2

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class HomeFragment : Fragment(R.layout.fragment_home), AdapterView.OnItemSelectedListener {

    // Declare the ActivityResultLaunchers
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateSpinner(view, R.id.spinner1, R.array.cap_diameter)
        populateSpinner(view, R.id.spinner2, R.array.cap_shape)
        populateSpinner(view, R.id.spinner3, R.array.cap_color)
        populateSpinner(view, R.id.spinner4, R.array.stem_width)

        // Initialize the ActivityResultLauncher
        val cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                val imageView: ImageView = view.findViewById(R.id.capturedImage)
                imageView.setImageBitmap(imageBitmap)
            }
        }

        // Initialize the permission ActivityResultLauncher
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Launch the camera.
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    cameraLauncher.launch(takePictureIntent)
                } catch (e: ActivityNotFoundException) {
                    // Handle the exception
                }
            } else {
                // Permission is denied. Handle the denial.
            }
        }

        val cameraButton: Button = view.findViewById(R.id.cameraButton)
        cameraButton.setOnClickListener {
            // Check if the camera permission is granted
            if (ContextCompat.checkSelfPermission(
                    requireContext(), android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Launch the camera
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    cameraLauncher.launch(takePictureIntent)
                } catch (e: ActivityNotFoundException) {
                    // Handle the exception
                }
            } else {
                // Request the permission using the permissionLauncher
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }

        // Declare the error text as a string resource
        val errorText = getString(R.string.error_text)

        val submitButton: Button = view.findViewById(R.id.submitButton)
        // Adjusted submitButton onClickListener
        submitButton.setOnClickListener {
            if (validateInputs()) {
                val mushroom = Mushroom(
                    capDiameter = getSpinnerValue(R.id.spinner1),
                    capShape = getSpinnerValue(R.id.spinner2),
                    capColor = getSpinnerValue(R.id.spinner3),
                    stemWidth = getSpinnerValue(R.id.spinner4),
                    photoUri = null, // Initialize with null
                    apiPoison = null, // Initialize with null
                    apiConfidence = null // Initialize with null
                )

                // Call the API and handle database insertion upon successful response
                callApiAndShowResult(mushroom)
            } else {
                Toast.makeText(context, "Validation failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Validation function
    private val defaultSpinnerValue = "..."

    private fun validateInputs(): Boolean {
        //if (!isPhotoSelected) return false // Check if a photo is selected

        // Check if all spinners have a value other than '...'
        val spinnerIds = listOf(R.id.spinner1, R.id.spinner2, R.id.spinner3, R.id.spinner4)
        return spinnerIds.all { spinnerId ->
            val spinner: Spinner = view?.findViewById(spinnerId) as Spinner
            spinner.selectedItem.toString() != defaultSpinnerValue
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Handle spinner item selection
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Handle case when nothing is selected in the spinner
    }

    private fun populateSpinner(view: View, spinnerId: Int, arrayResourceId: Int) {
        val spinner: Spinner = view.findViewById(spinnerId)
        val array = resources.getStringArray(arrayResourceId)

        // Add the default value at the beginning of the list
        //val arrayWithDefault = listOf("...") + array

        // Don't add default value, for testing purposes
        val arrayWithDefault = array

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayWithDefault
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    // Adjusted callApiAndShowResult function
    private fun callApiAndShowResult(mushroom: Mushroom) {
        val client = OkHttpClient()
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()

        // Your existing jsonObject creation
        val jsonObject = JSONObject().apply {
            put("cap_diameter", mushroom.capDiameter)
            put("cap_shape", mushroom.capShape)
            put("cap_color", mushroom.capColor)
            put("stem_width", mushroom.stemWidth)
            //put("photo_uri", mushroom.photoUri)
        }

        val body = jsonObject.toString().toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url("http://lts39.duckdns.org:8000/predict")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "API request failed: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.d("HomeFragment", "Request failed: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use { resp ->
                    val responseData = resp.body?.string()

                    try {
                        val jsonResponse = JSONObject(responseData)

                        // if the API returns a 200 status code, enter the following code block
                        if (resp.code == 200) {
                            activity?.runOnUiThread {
                                Log.d("HomeFragment", "Request successful")
                            }

                            val message = jsonResponse.optString("message")
                            val confidence = jsonResponse.optString("confidence")

                            var msgResponse = ""
                            if(message == "Deze paddenstoel is giftig") {
                                msgResponse = "P"
                            } else if (message == "Deze paddenstoel is eetbaar") {
                                msgResponse = "E"
                            }

                            mushroom.apiPoison = msgResponse
                            mushroom.apiConfidence = confidence

                            // Launch coroutine to insert mushroom into the database
                            lifecycleScope.launch(Dispatchers.IO) {
                                MushroomDatabase.getDatabase(requireContext()).mushroomDao().insertMushroom(mushroom)

                                // Switch to the ArchiveFragment
                                withContext(Dispatchers.Main) {
                                    val archiveFragment = ArchiveFragment()
                                    val transaction = parentFragmentManager.beginTransaction()
                                    transaction.replace(R.id.flFragment, archiveFragment)
                                    transaction.commit()
                                }

                            }



                        } else {
                            // throw an exception if the API returns a status code other than 200
                            throw IOException("Unexpected HTTP code $resp.code")
                        }


                    } catch (e: JSONException) {
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "API response parsing failed: ${e.message}", Toast.LENGTH_LONG).show()
                            Log.d("HomeFragment", "Error parsing response: ${e.message}")
                        }
                    }
                }
            }
        })


    }

    private fun getSpinnerValue(spinnerId: Int): String {
        val spinner: Spinner = view?.findViewById(spinnerId) as Spinner
        Log.d("HomeFragment", "Flag Spinner")
        return spinner.selectedItem.toString()
    }


}