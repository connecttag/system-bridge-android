package org.connecttag.lib.services.system.di

import org.connecttag.lib.services.system.ClipboardManagerImpl
import org.connecttag.lib.services.system.AndroidSystemInteractionLauncher
import org.connecttag.lib.services.system.IClipboardManager
import org.connecttag.lib.services.system.IKeyboardManager
import org.connecttag.lib.services.system.KeyboardManagerImpl
import org.connecttag.lib.services.system.SystemInteractionLauncher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
public abstract class SystemBridgeModule {

    @Binds
    @Singleton
    internal abstract fun bindClipboardManager(
        impl: ClipboardManagerImpl
    ): IClipboardManager

    @Binds
    @Singleton
    internal abstract fun bindKeyboardManager(
        impl: KeyboardManagerImpl
    ): IKeyboardManager

    @Binds
    @Singleton
    internal abstract fun bindSystemInteractionLauncher(
        impl: AndroidSystemInteractionLauncher
    ): SystemInteractionLauncher
}
