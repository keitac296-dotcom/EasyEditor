package com.easyeditor

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.easyeditor.databinding.ActivityEditorBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class EditorActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityEditorBinding
    private var mediaUri: Uri? = null
    private var isVideo = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        mediaUri = intent.getStringExtra("media_uri")?.let { Uri.parse(it) }
        isVideo = intent.getBooleanExtra("is_video", false)
        
        setupUI()
        
        mediaUri?.let {
            binding.previewImageView.setImageURI(it)
        }
        
        binding.btnCrop.setOnClickListener { cropMedia() }
        binding.btnFilter.setOnClickListener { applyFilter() }
        binding.btnExport.setOnClickListener { exportMedia() }
        binding.btnUndo.setOnClickListener { undo() }
    }
    
    private fun setupUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Mode sombre par défaut (comme CapCut)
        binding.root.setBackgroundColor(resources.getColor(android.R.color.black))
    }
    
    private fun cropMedia() {
        Toast.makeText(this, "Fonction crop - À implémenter", Toast.LENGTH_SHORT).show()
        // Utiliser FFmpeg pour recadrer
        // Exemple: FFmpeg.execute("-i input.mp4 -filter:v crop=720:1280:0:0 output.mp4")
    }
    
    private fun applyFilter() {
        // Afficher un BottomSheetDialog avec les filtres
        showFilterDialog()
    }
    
    private fun showFilterDialog() {
        val filters = listOf("Normal", "Noir/Blanc", "Sépia", "Vintage", "Cinéma")
        // TODO: Afficher un dialog avec RecyclerView des filtres
        // Utiliser FFmpeg avec: "-vf colorchannelmixer=.3:.4:.3:0:.3:.4:.3:0:.3:.4:.3"
    }
    
    private fun exportMedia() {
        lifecycleScope.launch {
            showProgress(true)
            val success = withContext(Dispatchers.IO) {
                performExport()
            }
            showProgress(false)
            
            if (success) {
                Toast.makeText(this@EditorActivity, "Export réussi !", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@EditorActivity, "Erreur d'export", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private suspend fun performExport(): Boolean {
        return try {
            val outputFile = File(cacheDir, "exported_${System.currentTimeMillis()}.mp4")
            val inputPath = mediaUri?.let { getPathFromUri(it) }
            
            // Commande FFmpeg simple pour copier sans modification
            val command = arrayOf("-i", inputPath, "-c", "copy", outputFile.absolutePath)
            val rc = FFmpeg.execute(command)
            rc == Config.RETURN_CODE_SUCCESS
        } catch (e: Exception) {
            false
        }
    }
    
    private fun getPathFromUri(uri: Uri): String {
        // TODO: Implémenter la conversion URI → chemin fichier
        return uri.path ?: ""
    }
    
    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) android.view.View.VISIBLE else android.view.View.GONE
    }
    
    private fun undo() {
        Toast.makeText(this, "Undo - À implémenter avec historique", Toast.LENGTH_SHORT).show()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}