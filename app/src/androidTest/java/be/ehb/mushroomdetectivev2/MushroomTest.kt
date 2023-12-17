package be.ehb.mushroomdetectivev2

import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.io.IOException
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class MushroomTest {

    @Test
    fun Sending_request_to_MushroomAPI_returns_status_200() {
        val client = OkHttpClient()
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        val jsonObject = JSONObject().apply {
            put("cap_diameter", "1.00")
            put("cap_shape", "B")
            put("cap_color", "D")
            put("stem_width", "0.50")
        }
        val body = jsonObject.toString().toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url("http://lts39.duckdns.org:8000/predict")
            .post(body)
            .build()
        var statusCode = 0

        // Create a CountDownLatch with a count of 1
        val latch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Decrement the count of the latch, releasing any waiting threads
                latch.countDown()
            }
            override fun onResponse(call: Call, response: Response) {
                statusCode = response.code
                // Decrement the count of the latch, releasing any waiting threads
                latch.countDown()
            }
        })
        latch.await()  // Wait for the CountDownLatch to reach zero
        assertEquals(200, statusCode)
    }

    @Test
    fun Sending_bad_request_to_MushroomAPI_fails() {
        val client = OkHttpClient()
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        val jsonObject = JSONObject().apply {
            // Send a bad request (no stem width)
            put("cap_diameter", "1.00")
            put("cap_shape", "B")
            put("cap_color", "D")
        }
        val body = jsonObject.toString().toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url("http://lts39.duckdns.org:8000/predict")
            .post(body)
            .build()
        var statusCode = 0

        // Create a CountDownLatch with a count of 1
        val latch = CountDownLatch(1)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Decrement the count of the latch, releasing any waiting threads
                latch.countDown()
            }
            override fun onResponse(call: Call, response: Response) {
                statusCode = response.code
                // Decrement the count of the latch, releasing any waiting threads
                latch.countDown()
            }
        })

        // Wait for the CountDownLatch to reach zero
        latch.await()

        assertNotEquals(200, statusCode)
    }

}