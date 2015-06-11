package kz.aksay.polygraph.uml;

/**
 
 @startuml
 
	actor Дизайнер
	actor Директор
	
	Директор -> (Список всех работ)
	Директор -> (Создать задачу)
	Директор -> (Назначить работу)
	
	:Дизайнер: -> (Список всех своих работ) 
	Дизайнер -> (Приступить к задаче)
	Дизайнер -> (Заполнить список работ в задаче)
 @enduml

 */
public class UseCases {

}
