package com.riju.drivertracker.ui.tripsettings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.riju.drivertracker.ui.uicomponents.DTOutlinedTextField

@Composable
fun ExpandableSwitch(
    textFieldValue: String?,
    textFieldLabel: String,
    supportingText: String? = null,
    switchText: String,
    switchDescription: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTextFieldChange: (String) -> Unit,
    textFieldCondition: Boolean? = null,
    textFieldTrailingIcon: (@Composable () -> Unit)? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
    OutlinedCard {
        SwitchCard(
            text = switchText,
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )

        AnimatedVisibility(visible = isChecked) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors().copy(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Text(text = switchDescription)
                    }
                }
                DTOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = textFieldValue ?: "",
                    isError = textFieldCondition == false,
                    onValueChange = onTextFieldChange,
                    supportingText = supportingText,
                    label = textFieldLabel,
                    trailingIcon = {
                        onTrailingIconClick?.let {
                            IconButton(onClick = onTrailingIconClick) {
                                textFieldTrailingIcon?.invoke()
                            }
                        }
                    }
                )
            }
        }
    }
}
