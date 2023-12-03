package be.ehb.mushroomdetectivev2

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
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

class HomeFragment : Fragment(R.layout.fragment_home), AdapterView.OnItemSelectedListener {

    // Declare the ActivityResultLaunchers
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateSpinner(view, R.id.spinner1, R.array.cap_diameter)
        populateSpinner(view, R.id.spinner2, R.array.cap_shape)
        populateSpinner(view, R.id.spinner3, R.array.cap_surface)
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
        submitButton.setOnClickListener {

            if (validateInputs()) {
                // If not all inputs are valid, display an error message
                Log.d("HomeFragment", "All inputs are valid")
            } else {
                // If all inputs are valid, display a success message
                Log.d("HomeFragment", errorText)
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
        val arrayWithDefault = listOf("...") + array

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayWithDefault
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }


}

