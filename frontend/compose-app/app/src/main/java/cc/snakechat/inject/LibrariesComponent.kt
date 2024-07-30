package cc.snakechat.inject

import cc.snakechat.imageloading.ImageLoadingComponent
import cc.snakechat.json.JsonComponent
import cc.snakechat.ktorclient.HttpClientComponent

interface LibrariesComponent :
    HttpClientComponent,
    JsonComponent,
    ImageLoadingComponent
