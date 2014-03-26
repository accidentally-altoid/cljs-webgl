(ns learningwebgl.lesson-02
  (:require
    [mat4]
    [learningwebgl.common :refer [init-gl init-shaders get-perspective-matrix get-position-matrix]]
    [cljs-webgl.buffers :refer [create-buffer clear-color-buffer draw!]]
    [cljs-webgl.misc :refer [capabilities]]
    [cljs-webgl.shaders :refer [get-attrib-location]]
    [cljs-webgl.constants :as const]
    [cljs-webgl.typed-arrays :as ta]))

(defn start []
  (let [canvas      (.getElementById js/document "canvas")
        gl          (init-gl canvas)
        shader-prog (init-shaders gl)
        triangle-vertex-position-buffer
                    (create-buffer gl
                      (ta/float32 [ 0.0,  1.0,  0.0,
                                   -1.0, -1.0,  0.0,
                                    1.0, -1.0,  0.0, ])
                      const/array-buffer
                      const/static-draw
                      3)

        triangle-vertex-color-buffer
                    (create-buffer gl
                      (ta/float32 [ 1.0,  0.0,  0.0,  1.0,
                                    0.0,  1.0,  0.0,  1.0,
                                    0.0,  0.0,  1.0,  1.0 ])
                      const/array-buffer
                      const/static-draw
                      4)

        square-vertex-position-buffer
                    (create-buffer gl
                      (ta/float32 [ 1.0,  1.0,  0.0,
                                   -1.0,  1.0,  0.0,
                                    1.0, -1.0,  0.0,
                                   -1.0, -1.0,  0.0])
                      const/array-buffer
                      const/static-draw
                      3)

        square-vertex-color-buffer
                    (create-buffer gl
                      (ta/float32 [ 0.5,  0.5,  1.0,  1.0,
                                    0.5,  0.5,  1.0,  1.0,
                                    0.5,  0.5,  1.0,  1.0,
                                    0.5,  0.5,  1.0,  1.0 ])
                      const/array-buffer
                      const/static-draw
                      4)

        vertex-position-attribute (get-attrib-location gl shader-prog "aVertexPosition")
        vertex-color-attribute (get-attrib-location gl shader-prog "aVertexColor")]

    (clear-color-buffer gl 0.0 0.0 0.0 1.0)

    (draw!
      gl
      :shader shader-prog
      :draw-mode const/triangles
      :capabilities {const/depth-test true}
      :count (.-numItems triangle-vertex-position-buffer)
      :attributes [{:buffer triangle-vertex-position-buffer :location vertex-position-attribute}
                   {:buffer triangle-vertex-color-buffer :location vertex-color-attribute}]
      :uniforms [{:name "uPMatrix" :type :mat4 :values (get-perspective-matrix gl)}
                 {:name "uMVMatrix" :type :mat4 :values (get-position-matrix [-1.5 0.0 -7.0])}])

    (draw!
      gl
      :shader shader-prog
      :draw-mode const/triangle-strip
      :count (.-numItems square-vertex-position-buffer)
      :attributes [{:buffer square-vertex-position-buffer :location vertex-position-attribute}
                   {:buffer square-vertex-color-buffer :location vertex-color-attribute}]
      :uniforms [{:name "uPMatrix" :type :mat4 :values (get-perspective-matrix gl)}
                 {:name "uMVMatrix" :type :mat4 :values (get-position-matrix [1.5 0.0 -7.0])}])))