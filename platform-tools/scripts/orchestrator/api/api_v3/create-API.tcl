package require java
java::import -package java.util ArrayList List
java::import -package java.util HashMap Map
java::import -package org.ekstep.graph.dac.model Node

set object_type "Api"
set graph_id "domain"

set create_response [createObject $graph_id $api $object_type]
return $create_response
