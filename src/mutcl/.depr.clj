(ns mutcl.depr)

;; deprecated prior to alpha
;; the original mutation scheme involved hiding the source file to me mutated as a dot-file
;; in this implementation, ._hiddenNAME, more precisely
;; however, when it became clear that recovering such a file from other possible dot-files
;; in the folder would be troublesome, since we'd need to keep track of which file was mutated
;; instead, the entire src directory is used as a template each time
  (defn hide-src-file
    ;; DEPR
    "hide src file by appending '._hidden_' to name"
    [file]
    (let [dest-path (str (.getParent file))
          copy-name (str "._hidden_" (.getName file))
          copy (io/file (str dest-path copy-name))]
      (io/copy file copy)))

;; deprecated as a redudancy prior to alpha
;; however, the preconditions are salvagable, likely
(defn clone-project
  ;; DEPR
  ;; DANGER: POTENTIALLY DELETES FILE CONTENTS FROM PROJECT FILE!!!
  "given a filename of a lein project, produce a mutant template"
  [project-name]
  (let
      [project (fs/absolute (str project-name))
       test-path (fs/absolute
                  (str (fs/name project) "/"
                       (fs/name project) "/"))
       src-path  (fs/absolute
                  (str (fs/name project) "/src/"
                       (fs/name project) "/"))]
    {:pre [(and
            (reduce #(and %1 %2) (map fs/exists?
                                      '(project test-path src-path)))
            (reduce #(and %1 %2) (map fs/directory?
                                      '(project test-path src-path)))
            (fs/child-of? project src-path)
            (fs/child-of? project test-path))]}
    ;; worked in terminal:
    ;; (fs/copy-dir (fs/absolute (fs/name fs/*cwd*)) (fs/absolute "../mutcl_1"))
    (fs/copy-dir project (str (fs/*cwd*) (fs/name project) "_temp"))))

;; deprecated prior to alpha
;; not sure what was the issue with this version of clone-project
(defn clone-project
  ;;DEPR
  "clones project to spare unforseen side-effects of mutation"
  [project-name]
  (let [project-file (io/file project-name)
        project-dir-name (str (.getParent project-file) (.getName project-file))
        temp-project-dir-name (str (.getParent project-file) (.getName project-file) ".bak")]
    (fs/copy project-dir-name temp-project-dir-name)))
