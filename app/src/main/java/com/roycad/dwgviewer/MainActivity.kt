package com.roycad.dwgviewer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.libredwg.DWGRenderer // Hypothetical import for DWG rendering
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var openFileButton: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupToolbar()
    }

    private fun setupViews() {
        openFileButton = findViewById(R.id.openFileButton)
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)

        openFileButton.setOnClickListener {
            openFilePicker()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "RoyCad DWG Viewer"
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/acad"  // MIME type for DWG files
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val uri = result.data?.data
            uri?.let {
                displayDWGFile(it)
            } ?: run {
                showError("Invalid file selected")
            }
        }
    }

    private fun displayDWGFile(uri: Uri) {
        showLoading(true)
        
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val filePath = getFilePathFromUri(uri)
                if (filePath != null) {
                    val dwgRenderer = DWGRenderer(this@MainActivity)
                    dwgRenderer.render(filePath)
                    
                    withContext(Dispatchers.Main) {
                        showLoading(false)
                        showSuccess("DWG file loaded successfully")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showError("Unable to process file path")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Error loading DWG file: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        return try {
            contentResolver.openFileDescriptor(uri, "r")?.use {
                // Get file path from URI using content resolver
                val cursor = contentResolver.query(uri, null, null, null, null)
                cursor?.use { c ->
                    if (c.moveToFirst()) {
                        val columnIndex = c.getColumnIndex("_data")
                        if (columnIndex != -1) {
                            return c.getString(columnIndex)
                        }
                    }
                }
            }
            uri.path  // Fallback to raw path if content resolver fails
        } catch (e: Exception) {
            null
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        openFileButton.isEnabled = !show
    }

    private fun showError(message: String) {
        showLoading(false)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}
