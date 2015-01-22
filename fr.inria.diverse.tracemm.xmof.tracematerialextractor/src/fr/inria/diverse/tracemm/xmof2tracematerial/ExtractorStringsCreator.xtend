package fr.inria.diverse.tracemm.xmof2tracematerial

import org.eclipse.emf.ecore.EClass
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity
import org.modelexecution.xmof.Syntax.Classes.Kernel.DirectedParameter

class ExtractorStringsCreator {
	
	public static val String ref_ExitToEntry = "correspondingEntryEvent"
	
	public static val String ref_EventToThis = "thisParam"
	
	static def String class_createEntryEventClassName(EClass confClass, Activity activity) {
		confClass.name.replace("Configuration", "") + "_" + activity.name + "EntryEventOccurrence"
	}

	static def String class_createExitEventClassName(EClass confClass, Activity activity) {
		confClass.name.replace("Configuration", "") + "_" + activity.name + "ExitEventOccurrence"
	}
	
	static def String ref_createEntryToParam(DirectedParameter param) {param.name + "Param"}
	static def String ref_createExitToReturn(DirectedParameter param) {param.name + "Return"}
}
