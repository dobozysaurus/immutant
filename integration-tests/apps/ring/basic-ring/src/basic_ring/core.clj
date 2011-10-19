(ns basic-ring.core
  (:require [immutant.registry :as service]))

(def shared? "app1")

(defn handler [request]
  (let [body (str "Hello From Clojure inside TorqueBox! This is basic-ring<pre>" shared? "</pre><img src='biscuit.jpg'/>")
        services (seq (map #(.getCanonicalName %) (.getServiceNames service/registry)))
        factory (service/service "jboss.naming.context.java.ConnectionFactory")]
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (str body "<pre>" services "</pre><p>" factory "</p>")}))
