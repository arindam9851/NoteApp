package com.example.noteapp.feature_note.presentation.notes

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.utils.NoteOrder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun List<Note>.groupAndSortByOrder(noteOrder: NoteOrder): Map<String, List<Note>> {
    // Sort notes according to order
    val sortedNotes = when (noteOrder) {
        is NoteOrder.Date -> if (noteOrder.ascending) this.sortedBy { it.timeStamp } else this.sortedByDescending { it.timeStamp }
        is NoteOrder.Title -> if (noteOrder.ascending) this.sortedBy { it.title.lowercase() } else this.sortedByDescending { it.title.lowercase() }
        is NoteOrder.Color -> if (noteOrder.ascending) this.sortedBy { it.color } else this.sortedByDescending { it.color }
    }

    // Group by date string
    return sortedNotes
        .groupBy { note ->
            Instant.ofEpochMilli(note.timeStamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        }
        // Sort headers descending (latest date first)
        .toSortedMap(compareByDescending { LocalDate.parse(it, DateTimeFormatter.ofPattern("MMM dd, yyyy")) })
}
