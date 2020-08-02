package me.lyc.fastcommon.arch

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.lyc.fastcommon.log.LogUtils

/**
 * Created by Liu Yuchuan on 2020/1/18.
 */
object FastCommonViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    private const val TAG = "ReaderViewModelFactory"
    private val createFactoryMap = hashMapOf<Class<out ViewModel>, IViewModelFactory>()
    private val cachedFactoryIndex =
        hashMapOf<IViewModelFactory, MutableList<Class<out ViewModel>>>()
    private val factories = hashSetOf<IViewModelFactory>()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val cachedFactory = createFactoryMap[modelClass]
        if (cachedFactory != null) {
            return cachedFactory.createViewMode(modelClass)!!
        }

        for (factory in factories) {
            val viewModel = factory.createViewMode(modelClass)
            if (viewModel != null) {
                if (factory.shouldCache()) {
                    createFactoryMap[modelClass] = factory
                    val list = cachedFactoryIndex[factory] ?: mutableListOf()
                    list.add(modelClass)
                    cachedFactoryIndex[factory] = list
                }
                return viewModel
            }
        }

        LogUtils.d(
            TAG,
            "Cannot create $modelClass by IViewModelFactory! Use default new instance!"
        )

        return super.create(modelClass)
    }

    @MainThread
    fun registerFactory(factory: IViewModelFactory) {
        factories.add(factory)
    }

    @MainThread
    fun unregisterFactory(factory: IViewModelFactory) {
        factories.remove(factory)
        cachedFactoryIndex.remove(factory)?.forEach {
            createFactoryMap.remove(it)
        }
    }
}
