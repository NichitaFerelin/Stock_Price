package com.ferelin.features.authentication

import com.ferelin.core.coroutine.DispatchersProvider
import com.ferelin.core.domain.usecase.AuthUseCase
import com.ferelin.core.network.NetworkListener
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class LoginScope

@LoginScope
@Component(dependencies = [LoginDeps::class])
interface LoginComponent {
  @Component.Builder
  interface Builder {
    fun dependencies(deps: LoginDeps): Builder
    fun build(): LoginComponent
  }

  fun viewModelFactory(): LoginViewModelFactory
}

interface LoginDeps {
  val authUseCase: AuthUseCase
  val networkListener: NetworkListener
  val dispatchersProvider: DispatchersProvider
}