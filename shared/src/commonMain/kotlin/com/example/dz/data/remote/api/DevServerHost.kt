package com.example.dz.data.remote.api

/**
 * Host that reaches a server running on the developer's own machine.
 *
 * It differs per platform: the Android emulator is a virtual machine with its
 * own loopback, so it reaches the host through an alias, while the iOS
 * simulator shares the Mac's network and can use loopback directly.
 *
 * Neither value works from a physical device — put the machine's LAN address in
 * [ApiConfig.baseUrl] for that, or point it at a deployed server.
 */
expect val devServerHost: String
