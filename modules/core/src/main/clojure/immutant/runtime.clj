;; Copyright 2008-2011 Red Hat, Inc, and individual contributors.
;; 
;; This is free software; you can redistribute it and/or modify it
;; under the terms of the GNU Lesser General Public License as
;; published by the Free Software Foundation; either version 2.1 of
;; the License, or (at your option) any later version.
;; 
;; This software is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
;; Lesser General Public License for more details.
;; 
;; You should have received a copy of the GNU Lesser General Public
;; License along with this software; if not, write to the Free
;; Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
;; 02110-1301 USA, or see the FSF site: http://www.fsf.org.

(ns immutant.runtime
  "This namespace is solely for the use of ClojureRuntime. You
   should never require it in clojure code."
  (:require [immutant.utilities :as util])
  (:require [immutant.registry :as registry])
  (:require [clojure.java.io :as io])
  (:require [clojure.tools.logging :as log]))

(defn require-and-invoke [namespaced-fn & [args]]
  "Takes a string of the form \"namespace/fn\", requires the namespace, then invokes fn"
  (apply (util/require-and-intern namespaced-fn) args))

(defn initialize [init-fn]
  "Attempts to initialize the app by calling an init-fn (if given) or, lacking that, tries to load an immutant.clj from the app-root"
  (let [config-file (io/file (registry/fetch "app-root") "immutant.clj")
        config-exists (.exists config-file)]
    (if init-fn
      (do
        (if config-exists
          (log/warn "immutant.clj found in" (registry/fetch "app-name") ", but you specified an :init fn; ignoring immutant.clj"))
        (require-and-invoke init-fn))
      (if config-exists
        (load-file (.getAbsolutePath config-file))
        (log/warn "no immutant.clj found in" (registry/fetch "app-name") "and you specified no init fn; no app initialization will be performed")))))

