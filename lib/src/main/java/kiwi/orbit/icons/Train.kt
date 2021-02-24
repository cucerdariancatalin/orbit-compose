package kiwi.orbit.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import kiwi.orbit.R

public val Icons.Train: Painter
	@Composable
	get() {
		if (icon != null) return icon!!
		icon = painterResource(R.drawable.ic_train)
		return icon!!
	}

private var icon: Painter? = null
