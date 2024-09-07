package com.example.jetnote.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnote.R
import com.example.jetnote.components.NoteButton
import com.example.jetnote.components.NoteInputText
import com.example.jetnote.data.NotesDataSource
import com.example.jetnote.model.Note
import com.example.jetnote.util.formatDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    notes: List<Note>,
    onAddNote: (Note) -> Unit,
    onRemoveNote: (Note) -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    Column(modifier = Modifier.padding(6.dp)) {
        TopAppBar(title = {
            Text(text = "Briefly", fontFamily = FontFamily.Monospace)
        } , actions = {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Icon")
        },  colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.LightGray,
        ))
            //Content
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            NoteInputText(
                modifier = Modifier.padding(top = 9.dp , bottom = 8.dp),
                text = title,
                label = "Title",
                onTextChange = {
                    if (it.all { char ->
                        char.isLetter() || char.isWhitespace()
                        }) title = it
                } )

            NoteInputText(
                modifier = Modifier.padding(top = 9.dp , bottom = 8.dp),
                text = description,
                label = "Note",
                onTextChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) description = it
                } )

            NoteButton(text = "Save",
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty()) {
                        //save
                        onAddNote(Note(title = title ,
                            description = description))
                        title = ""
                        description = ""
                        Toast.makeText(context , "Note Saved" , Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        Divider(modifier = Modifier.padding(10.dp))
        LazyColumn {
            items(notes) { note ->
                NoteRow(note = note , onNoteClicked = {
                    onRemoveNote(note)
                })
            }
        }
    }
}

@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClicked: (Note) -> Unit
) {
        Surface(
            modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(13.dp))
                .fillMaxWidth(),
            color = Color.LightGray
        ) {
                Column(
                    modifier
                        .clickable { onNoteClicked(note) }
                        .padding(6.dp),
                    horizontalAlignment = Alignment.Start)
                {
                    Text(note.title, style = MaterialTheme.typography.titleMedium , fontFamily = FontFamily.Monospace)
                    Text(note.description, style = MaterialTheme.typography.bodyMedium , fontFamily = FontFamily.Monospace)
                    Text(formatDate(note.entryTime.time),
                        style = MaterialTheme.typography.labelMedium , fontFamily = FontFamily.Monospace)
                }
        }
}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    NoteScreen(notes = NotesDataSource().loadNotes(), onAddNote = {} , onRemoveNote = {})
}