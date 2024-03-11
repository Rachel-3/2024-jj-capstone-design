package com.example.blur.components.TextField

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.blur.R

@Composable
fun AddDeviceTextField(onValueChange: (String) -> Unit) {

    val idRegex = Regex("^[A-Z0-9\\-]{1,}\$")
    // ID 검사 함수
    fun isValidDevice(id: String): Boolean {
        return idRegex.matches(id)
    }

    var DeviceId by remember { mutableStateOf("") }
    val isErrorInDeviceId by remember {
        derivedStateOf {
            if (DeviceId.isEmpty()) {
                false
            } else {
                !isValidDevice(DeviceId)
            }
        }
    }
    Column {
        OutlinedTextField(
            value = DeviceId,
            onValueChange = {
                DeviceId = it
                onValueChange(it) // 새로운 값이 입력될 때마다 외부로 알림
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(text = "제품코드") },
            placeholder = { Text("제품코드를 입력하세요.") },
            supportingText = {
                if (isErrorInDeviceId) {
                    Text(text = "제품코드는 영대문자, 숫자, - 만  가능합니다.")
                }
            },
            isError = isErrorInDeviceId,
            trailingIcon = {
                if (isErrorInDeviceId) {
                    Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 텍스트 사이의 간격 조정
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_error_24),
                        contentDescription = "에러", // 아이콘 설명
                        tint = Color.Red
                    )
                }
            }
        )

    }
}
