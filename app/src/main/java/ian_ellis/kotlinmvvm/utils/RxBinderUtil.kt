package ian_ellis.kotlinmvvm.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import java.util.HashMap
import java.util.HashSet
import kotlin.properties.Delegates

/**
 * Created by ttuo on 02/08/14.
 * Converted to Kotlin
 */
public class RxBinderUtil(target: Any) {

  private val BINDER_TAG: String = javaClass.canonicalName
  private var targetTag: String by Delegates.notNull()
  private val compositeSubscription = CompositeSubscription()
  private val registeredCallbacks = HashMap<Any, RegisteredCallbacksSet<out Any>>()
  private val handler = Handler(Looper.getMainLooper())

  init {
    targetTag = target.javaClass.canonicalName
  }

  public fun clear() {
    compositeSubscription.clear()
    for ((sender, registeredCallbacksSet) in registeredCallbacks) {
      registeredCallbacksSet.clear()
    }
    registeredCallbacks.clear()
  }

  public fun <U> bindProperty(
    observable: Observable<U>,
    setter: ((value: U) -> Unit)?,
    error: ((value: Throwable) -> Unit)? = null,
    complete: (() -> Unit)? = null
  ) {
    compositeSubscription.add(subscribeSetter(targetTag, observable, setter, error, complete))
  }

  public fun <U: Any> bindCallback(
    sender: U,
    registerFunction: U.(((Throwable) -> Unit)?) -> Unit,
    callback: (Throwable) -> Unit
  ) {
    var callbacksForSender = registeredCallbacks.get(sender)
    if (callbacksForSender == null) {
      callbacksForSender = RegisteredCallbacksSet(sender)
      registeredCallbacks.put(sender as Any, callbacksForSender)
    }
    callbacksForSender.registerCallback(registerFunction, callback)
  }

  private fun <U> subscribeSetter(
    tag: String, observable: Observable<U>,
    setter: ((U) -> Unit)?,
    error: ((value: Throwable) -> Unit)? = null,
    complete: (() -> Unit)? = null
  ): Subscription {
    return observable.observeOn(AndroidSchedulers.mainThread())
      .subscribe(SetterSubscriber(tag, setter, error, complete))
  }

  private inner class SetterSubscriber<U> : Subscriber<U> {

    private var tag: String by Delegates.notNull()
    private var setter: ((value: U) -> Unit)? = null

    private var error: ((value: Throwable) -> Unit)? = null
    private var complete: (() -> Unit)? = null

    constructor(
      tag: String,
      setter: ((value: U) -> Unit)?,
      error: ((value: Throwable) -> Unit)?,
      complete: (() -> Unit)?
    ) {
      this.tag = tag
      this.setter = setter
      this.error = error
      this.complete = complete
    }

    override fun onCompleted() {
      Log.v(BINDER_TAG, tag + "." + "onCompleted")

      val c = complete ?: return
      c.let { c() }
    }

    override fun onError(e: Throwable) {
      Log.e(BINDER_TAG, tag + "." + "onError", e)

      val er = error ?: return
      er.let { er(e) }
    }

    override fun onNext(u: U) {
      val s = setter ?: return
      s.let { s(u) }
    }

  }

  private inner class RegisteredCallbacksSet<T>(private val instance: T) {

    private val callbacks = HashSet<T.(((Throwable) -> Unit)?) -> Unit>()

    // wrap callback in handler to ensure it's always on the UI thread
    public fun registerCallback(registerFunction: T.(((Throwable) -> Unit)?) -> Unit, callback: (Throwable) -> Unit) {
      instance.registerFunction { handler.post { callback.invoke(it) } }
      callbacks.add(registerFunction)
    }

    public fun unregisterCallback(registerFunction: T.(((Throwable) -> Unit)?) -> Unit) {
      instance.registerFunction(null)
      callbacks.remove(registerFunction)
    }

    public fun clear() {
      callbacks.forEach {
        instance.it(null)
      }
    }

    override fun equals(other: Any?): Boolean {
      return if (other is RegisteredCallbacksSet<*>) {
        instance != null && instance.equals(other.instance)
      } else {
        false
      }
    }

  }

}

