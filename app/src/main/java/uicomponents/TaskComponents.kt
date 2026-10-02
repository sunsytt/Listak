package uicomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.test.espresso.base.Default
import model.Priority
import model.Task

@Composable
fun SummaryCard(
    pendingCount: Int,
    completedCount: Int,
    progressPercent: Int,
    modifier: Modifier = Modifier,
    stringResourse: (Any?) -> Unit
) {
    val barColor = when{
        progressPercent < 50 -> MaterialTheme.colorScheme.error
        progressPercent < 80 -> MaterialTheme.colorScheme.warning
        else -> MaterialTheme.colorScheme.primary
    }
    Card(modifier=modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResourse(R.string.summary_title)
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = stringResource(R.string.summary_pending, pendingCount))
                Text(text = stringResource(R.string.summary_completed, completedCount))
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = {progressPercent/100},
                color = barColor,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.summary_progress, progressPercent))
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier
){
    val priorityColor = when(task.priority){
        Priority.ALTA -> MaterialTheme.colorScheme.error
        Priority.MEDIA -> MaterialTheme.colorScheme.warning
        Priority.BAJA -> MaterialTheme.colorScheme.primary
    }
    val titleColor = if (task.done) {
        MaterialTheme.colorScheme.onSurfaceVariant
    }else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(modifier = modifier.fillMaxWidth()){
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Checkbox(checked = task.done, onCheckedChange = {onToggle()})
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    color = titleColor,
                    textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text = stringResourse(task.priority.labelRes),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Box(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(12.pd)
                    .background(color = priorityColor, shape = CircleShape)
            )
            IconButton(onClick = onDeleted){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_task)
                )
            }
        }
    }
}

class Icons(imageVector: Any, contentDescription: String) {

}
