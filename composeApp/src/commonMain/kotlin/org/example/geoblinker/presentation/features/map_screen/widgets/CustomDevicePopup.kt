package org.example.geoblinker.presentation.features.map_screen.widgets


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*


@Composable
fun CustomDevicePopup(
    viewModel: DeviceViewModel,
    selectedMarker: Device?,
    webView: WebView,
    onChangeValueToNull: () -> Unit,
    toDeviceScreen: (Device) -> Unit
) {
    val unitsDistance by viewModel.unitsDistance


    selectedMarker?.let { item ->
        //var isShowAdd by remember { mutableStateOf(false) }
        var isShowDiagnosis by remember { mutableStateOf(false) }
        var isShowComments by remember { mutableStateOf(false) }
        //val offsetYAnimated by animateIntAsState(
        //    targetValue = if (isShowAdd) (-414).sdp().value.toInt() else (-290).sdp().value.toInt(),
        //    label = ""
        //)


        webView.evaluateJavascript(
            "setCenter(${item.lat}, ${item.lng})",
            null
        )


        Dialog(
            onChangeValueToNull,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onChangeValueToNull() },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.padding(bottom = 196.sdp())
                ) {
                    Surface(
                        modifier = width(332.sdp())
                            .animateContentSize(),
                        shape = ComicBubbleShape(
                            cornerRadius = 10.sdp(),
                            pointerHeight = 24.sdp(),
                            pointerWidth = 20.sdp()
                        ),
                        color = Color(0xFFDAD9D9)
                    ) {
                        Column {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        //if (isShowAdd) {
                                        onChangeValueToNull()
                                        toDeviceScreen(item)
                                        //}
                                        //isShowAdd = true
                                    },
                                shape = RoundedCornerShape(10.sdp()),
                                color = Color.White,
                                shadowElevation = 4.sdp()
                            ) {
                                Column(
                                    modifier = Modifier.padding(bottom = 24.sdp())
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(15.sdp()),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            size(9.sdp()).background(
                                                Color(0xFF12CD4A),
                                                MaterialTheme.shapes.small
                                            )
                                        )
                                        Spacer(width(11.sdp()))
                                        if (item.name.isNotEmpty()) {
                                            Text(
                                                item.name,
                                                modifier = Modifier.weight(1f),
                                                overflow = TextOverflow.Ellipsis,
                                                maxLines = 1,
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        } else {
                                            Text(
                                                stringResource(R.string.an_unnamed_device),
                                                modifier = Modifier.weight(1f),
                                                color = Color(0xFF737373),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                        Icon(
                                            imageVector = ImageVector.vectorResource(
                                                when {
                                                    item.signalRate <= 20 -> R.drawable.signal_low
                                                    item.signalRate <= 60 -> R.drawable.signal_half
                                                    else -> R.drawable.signal_strength
                                                }
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.sdp(), 17.sdp()),
                                            tint = Color.Unspecified
                                        )
                                    }
                                    HorizontalDivider(
                                        Modifier.fillMaxWidth()
                                            .padding(horizontal = 15.sdp()),
                                        1.sdp(),
                                        Color(0xFFDAD9D9).copy(alpha = 0.5f)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(15.sdp()),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            item.modelName,
                                            modifier = Modifier.weight(1f),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Icon(
                                            imageVector = ImageVector.vectorResource(
                                                when {
                                                    item.powerRate <= 25 -> R.drawable.battery_quarter
                                                    item.powerRate <= 50 -> R.drawable.battery_half
                                                    else -> R.drawable.battery_full
                                                }
                                            ),
                                            contentDescription = null,
                                            modifier = size(24.sdp()),
                                            tint = Color.Unspecified
                                        )
                                    }
                                    HorizontalDivider(
                                        Modifier.fillMaxWidth()
                                            .padding(horizontal = 15.sdp()),
                                        1.sdp(),
                                        Color(0xFFDAD9D9).copy(alpha = 0.5f)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(15.sdp()),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            formatSpeed(item.speed, unitsDistance),
                                            modifier = Modifier.weight(1f),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Icon(
                                            imageVector = Icons.Filled.Speed,
                                            contentDescription = null,
                                            modifier = size(24.sdp()),
                                            tint = Color(0xFF12CD4A)
                                        )
                                    }
                                }
                            }
                            /*
                    if (isShowAdd) {
                        Column(
                            modifier = Modifier.padding(15.sdp())
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.setDevice(item)
                                        isShowDiagnosis = true
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    imageVector = when(item.typeStatus) {
                                        Device.TypeStatus.Available -> Icons.Filled.LocationOn
                                        Device.TypeStatus.Ready -> Icons.Filled.Home
                                        Device.TypeStatus.RequiresRepair -> Icons.Filled.Build
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(24.sdp()),
                                    tint = Color(0xFF12CD4A)
                                )
                                Text(
                                    TimeUtils.formatToLocalTime(item.bindingTime),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            HorizontalDivider(
                                Modifier.fillMaxWidth().padding(vertical = 15.sdp()),
                                1.sdp(),
                                Color(0xFFDAD9D9).copy(alpha = 0.5f)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Speed,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.sdp()),
                                        tint = Color(0xFF12CD4A)
                                    )
                                    Spacer(Modifier.width(12.sdp()))
                                    Text(
                                        formatSpeed(10.0, unitsDistance),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                                Row(
                                    modifier = Modifier.clickable { isShowComments = true },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    for (i in 1..5) {
                                        Icon(
                                            imageVector = Icons.Filled.StarRate,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.sdp()),
                                            tint = ColorStar
                                        )
                                        Spacer(Modifier.width(4.sdp()))
                                    }
                                    Spacer(Modifier.width(8.sdp()))
                                    Text(
                                        "8",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                     */
                        }
                    }
                }
            }
        }


        if (isShowDiagnosis) {
            CustomDiagnosisPopup(
                item,
                {
                    if (it in arrayOf(Device.TypeStatus.Available, Device.TypeStatus.Ready))
                        viewModel.updateDevice(item.copy(
                            typeStatus = it,
                            breakdownForecast = null,
                            maintenanceRecommendations = null
                        ))
                    else
                        viewModel.updateDevice(item.copy(typeStatus = it))
                },
                { isShowDiagnosis = false },
                listOf(
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                    item.bindingTime,
                )
            )
        }


        if (isShowComments) {
            CustomCommentsPopup(
                { isShowComments = false }
            )
        }
    }
