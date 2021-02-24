package kiwi.orbit.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import kiwi.orbit.R

public val Icons.FlightDirect: Painter
	@Composable
	get() {
		if (icon != null) return icon!!
		icon = painterResource(R.drawable.ic_flight_direct)
		return icon!!
	}

private var icon: Painter? = null
