package com.example.firebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebase.models.Mark
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Abraham
 * This class was created on 13/06/2019
 * This is a small example to use cloud firestore with kotlin
 */
class MainActivity : AppCompatActivity() {

    //declare val for save the collection
    private val marksCollection: CollectionReference


    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        marksCollection = FirebaseFirestore.getInstance().collection("marks")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //init
        save_button.setOnClickListener {
            val name = name_editText.text.toString()
            val group = group_editText.text.toString()
            val mark = mark_editText.text.toString()
            if (isValidText(name, group, mark)) {
                saveMark(Mark(name, group, mark.toDouble()))
            } else {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show()
            }//end if
        }//end listener

        //show the new date add before
        addMarksListener()
    }//end for onCreate

    /**
     * @param mark
     */
    private fun saveMark(mark: Mark) {
        //add the collection and save the Mark, this is validated
        marksCollection.add(mark).addOnSuccessListener {
            Toast.makeText(this, "Regsistro guardado", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error guardando el registro", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addMarksListener() {
        marksCollection.addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    addChanges(changes)
                }
            } else {
                Toast.makeText(this, "Ha ocurrido un error leyendo las notas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * @param changes
     */
    private fun addChanges(changes: List<DocumentChange>) {
        for (change in changes) {
            if (change.type == DocumentChange.Type.ADDED) {
                addToList(change.document.toObject(Mark::class.java))
            }
        }
    }

    /**
     * @param mark
     */
    private fun addToList(mark: Mark) {
        var text = markList_textView.text.toString()
        text += mark.toString() + "\n"
        markList_textView.text = text
    }

    private fun isValidText(name: String, group: String, mark: String): Boolean {
        return !name.isNullOrEmpty() &&
                !group.isNullOrEmpty() &&
                !mark.isNullOrEmpty()
    }

}
