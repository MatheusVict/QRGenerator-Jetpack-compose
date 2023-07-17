package dev.matheusvict.qrcodegenerate

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import dev.matheusvict.qrcodegenerate.ui.theme.PurpleGrey80
import dev.matheusvict.qrcodegenerate.ui.theme.QrCodeGenerateTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun QrCodeApp() {
    var textValue by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var qrCodeGenerated by remember {
        mutableStateOf<Bitmap?>(null)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Gerador de QR Code", textAlign = TextAlign.Center)

            if (qrCodeGenerated != null) {
                Image(
                    bitmap = qrCodeGenerated!!.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.size(220.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                    contentDescription = "",
                    modifier = Modifier.size(200.dp)
                )
            }


        }

        Column {

            TextField(
                value = textValue,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = PurpleGrey80,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Entre com um texto",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black
                    )
                },
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    textColor = Color.Black
                ),
                onValueChange = {
                    textValue = it
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            RoundedButton(
                onClick = {
                    qrCodeGenerated = generateQrCode(textValue.text)
                },
                enabled = textValue.text.isNotEmpty(),
                text = "Gerar QR Code",
                color = Color.Green
            )
        }
    }
}

fun generateQrCode(text: String): Bitmap {
    val matrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, 512, 512)
    return convertMatrixToBitmap(matrix)
}
fun convertMatrixToBitmap(matrix: BitMatrix): Bitmap {
    val pixelWidth = matrix.width
    val pixelHeight = matrix.height

    val bitmap = Bitmap.createBitmap(pixelWidth, pixelHeight, Bitmap.Config.RGB_565)

    for (y in 0 until pixelHeight) {
        for (x in 0 until pixelWidth) {
            bitmap.setPixel(
                x,
                y,
                if (matrix.get(x, y))
                    android.graphics.Color.BLACK
                else
                    android.graphics.Color.WHITE
            )
        }
    }
    return bitmap
}

@Composable
fun RoundedButton(
    onClick: () -> Unit,
    enabled: Boolean,
    text: String,
    color: Color
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.Black
        )
    ) {
        Text(
            text,
            modifier = Modifier.padding(vertical = 6.dp),
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QRCodeAppPreview() {
    QrCodeGenerateTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            QrCodeApp()
        }
    }
}