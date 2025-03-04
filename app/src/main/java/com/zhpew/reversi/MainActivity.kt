package com.zhpew.reversi

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import com.zhpew.reversi.view.BoardView

class MainActivity : FragmentActivity() {

    private val controller: GameController = GameController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenAdapter()
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    fun ScreenAdapter() {
        val windowSizeClass = calculateWindowSizeClass(activity = this)
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> PhoneLayout()  // 手机
            WindowWidthSizeClass.Medium, // 小平板
            WindowWidthSizeClass.Expanded -> PhoneLayout() // 大平板
        }
    }

    @Composable
    fun PhoneLayout() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (controller.state.value.isRedTurn) "红色回合" else "蓝色回合",
                Modifier.padding(vertical = 40.dp),
                fontSize = 20.sp,
                color = colorResource(id = R.color.black)
            )

            Box {
                BoardView(controller.state.value, controller.onClick)
            }
            if (controller.state.value.showEndView) {
                AlertDialog(
                    onDismissRequest = {
                        controller.initGame()
                    },
                    text = {
                        Text(text = if (controller.state.value.BlueCount > controller.state.value.RedCount) "蓝方获胜！" else "红方获胜！")
                    },
                    confirmButton = {
                        Text(text = "确定", Modifier.clickable {
                            controller.initGame()
                        })
                    },
                )
            }
        }
    }

}