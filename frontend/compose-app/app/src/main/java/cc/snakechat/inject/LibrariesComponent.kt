package cc.snakechat.inject

import cc.snakechat.json.JsonComponent
import cc.snakechat.ktorclient.HttpClientComponent

interface LibrariesComponent :
    HttpClientComponent,
    JsonComponent