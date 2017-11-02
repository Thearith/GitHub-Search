package thearith.github.com.github_search.view.internal.di

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Scope

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the application to be memorized in the
 * correct component.
 *
 * Its concept is similar to a Singleton instance
 */
@Scope
@Retention(RetentionPolicy.CLASS)
annotation class ApplicationScope
