package kiwi.orbit.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import kiwi.orbit.R

public val Icons.Document: Painter
	@Composable
	get() {
		if (icon != null) return icon!!
		icon = painterResource(R.drawable.ic_document)
		return icon!!
	}

private var icon: Painter? = null
