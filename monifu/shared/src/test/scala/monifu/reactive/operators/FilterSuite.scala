/*
 * Copyright (c) 2015 Alexandru Nedelcu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package monifu.reactive.operators

import monifu.reactive.{DummyException, Observable}
import scala.concurrent.duration.Duration

object FilterSuite extends BaseOperatorSuite {
  val waitForFirst = Duration.Zero
  val waitForNext = Duration.Zero

  def count(sourceCount: Int) = {
    sourceCount
  }

  def sum(sourceCount: Int): Long =
    sourceCount.toLong * (sourceCount + 1)
 
  def observable(sourceCount: Int) = {
    require(sourceCount > 0, "sourceCount should be strictly positive")
    Some {
      if (sourceCount == 1)
        Observable.unit(2L).filter(_ % 2 == 0)
      else
        Observable.range(1, sourceCount * 2 + 1, 1).filter(_ % 2 == 0)
    }
  }

  def observableInError(sourceCount: Int, ex: Throwable) = {
    require(sourceCount > 0, "sourceCount should be strictly positive")
    Some {
      val ex = DummyException("dummy")
      if (sourceCount == 1)
        createObservableEndingInError(Observable.unit(2L), ex)
          .filter(_ % 2 == 0)
      else
        createObservableEndingInError(Observable.range(1, sourceCount * 2 + 1, 1), ex)
          .filter(_ % 2 == 0)
    }
  }

  def brokenUserCodeObservable(sourceCount: Int, ex: Throwable) = {
    require(sourceCount > 0, "sourceCount should be strictly positive")
    Some {
      if (sourceCount == 1)
        Observable.unit(1L).filter(_ => throw ex)
      else
        Observable.range(1, sourceCount * 2 + 1, 1).filter { x =>
          if (x == sourceCount * 2)
            throw ex
          else
            x % 2 == 0
        }
    }
  }
}