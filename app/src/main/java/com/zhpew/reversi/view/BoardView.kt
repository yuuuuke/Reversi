package com.zhpew.reversi.view

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.zhpew.reversi.GameController.Companion.EMPTY_PIECE
import com.zhpew.reversi.GameController.Companion.RED_PIECE
import com.zhpew.reversi.MyApplication
import com.zhpew.reversi.UiState
import com.zhpew.reversi.px

val CellSize: Int by lazy {
    val display = MyApplication.instance.resources.displayMetrics
    if (display.widthPixels > display.heightPixels) {
        ((display.heightPixels - 20) / 8 / display.density + 0.5).toInt()
    } else {
        ((display.widthPixels - 20) / 8 / display.density + 0.5).toInt()
    }
}

@Composable
fun BoardView(uiState: UiState) {
    Canvas(
        modifier = Modifier
            .width((CellSize * 8).dp)
            .height((CellSize * 8).dp)
            .pointerInput(Unit){
                detectTapGestures {
                    Log.v("zwp","${it.x}//"+it.y)
                }
            }
    ) {
        drawIntoCanvas {
            val paint = Paint()
            paint.color = Color.Blue
            paint.style = PaintingStyle.Stroke
            //先画棋盘
            val rect = Rect(0f, 0f, CellSize.px * 8f, CellSize.px * 8f)
            it.drawRect(rect, paint)
            for (i in 1 until 8) {
                //横竖各七条线
                it.drawLine(
                    Offset(0f, i * CellSize.px * 1f),
                    Offset(8f * CellSize.px, i * CellSize.px * 1f),
                    paint
                )
                it.drawLine(
                    Offset(i * CellSize.px * 1f, 0f),
                    Offset(i * CellSize.px * 1f, 8f * CellSize.px),
                    paint
                )
            }
            for(i in 0 until uiState.Pieces.size){
                if(uiState.Pieces[i] != EMPTY_PIECE){
                    val column = i%8
                    val row = i/8
                    paint.color = if(uiState.Pieces[i] == RED_PIECE) Color.Red else Color.Blue
                    paint.style = PaintingStyle.Fill
                    val offset = Offset(column* CellSize.px+ CellSize.px*0.5f,row* CellSize.px*1f+ CellSize.px*0.5f)
                    it.drawCircle(offset, CellSize.px*0.3f,paint)
                    paint.color = Color.White
                    it.drawCircle(offset,2f,paint)
                }
            }
        }
    }
}