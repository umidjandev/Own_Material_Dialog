package com.najdimu.ownmaterialdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.najdimu.own_material_dialog.MaterialDialogOwn
import com.najdimu.ownmaterialdialog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var debugMode = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.basicTitledButtons.setOnClickListener {
            MaterialDialogOwn(this).show {
                title(R.string.useGoogleLocationServices)
                message(R.string.useGoogleLocationServicesPrompt)
                positiveButton(R.string.agree)
                negativeButton(R.string.disagree)
                debugMode(debugMode)
            }
        }




    }
}