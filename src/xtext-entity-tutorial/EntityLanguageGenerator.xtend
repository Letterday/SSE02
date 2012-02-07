/*
 * Non-broken version of the code generator from http://www.eclipse.org/Xtext/documentation/2_1_0/040-first-code-generator.php
 * updated to xtext 2.2.  Some names are specific to the way the language grammar is defined (which was done slightly differently
 * from the basci tutorial)
 */

/*
 * (initially generated by Xtext)
 */
package dk.sdu.mmmi.sse02.tutorial2.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IFileSystemAccess

import static extension org.eclipse.xtext.xbase.lib.IteratorExtensions.*
import dk.sdu.mmmi.sse02.tutorial2.entityLanguage.Entity
import org.eclipse.xtext.naming.IQualifiedNameProvider
import javax.inject.Inject
import dk.sdu.mmmi.sse02.tutorial2.entityLanguage.Feature

class EntityLanguageGenerator implements IGenerator {
	
	@Inject extension IQualifiedNameProvider nameProvider 
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		for(e: resource.getAllContents().toIterable().filter(typeof(Entity))) {
			fsa.generateFile(e.getFullyQualifiedName.toString.replace(".", "/") + ".java",e.compile)
		}
	}
	
	def compile(Entity e) '''
	// Auto-generated code
	�IF e.eContainer.eClass().name.equals("Package")�
	package �e.eContainer.getFullyQualifiedName�;
	�ELSE�
	// Default package
	�ENDIF�
	import java.util.List;
	import java.util.ArrayList;
	
	public class �e.name� �IF e.esupertype!=null� extends �e.esupertype.getFullyQualifiedName� �ENDIF� {
		�FOR f:e.features�
			�f.compile�
		�ENDFOR�
	}
    '''
    
    def compile(Feature f) '''
    �IF !f.many�
    private �f.type.getFullyQualifiedName� �f.name�;
    public void set�f.name.toFirstUpper�(�f.type.getFullyQualifiedName� value) { �f.name� = value; }
    public �f.type.getFullyQualifiedName� get�f.name.toFirstUpper�() { return �f.name�; }
    �ELSE�
    private List<�f.type.getFullyQualifiedName�> �f.name� = new ArrayList<�f.type.getFullyQualifiedName�>();
    public void add�f.name.toFirstUpper�(�f.type.getFullyQualifiedName� value) { �f.name�.add(value); }
    public List<�f.type.getFullyQualifiedName�> get�f.name.toFirstUpper�() { return �f.name�; }
    �ENDIF�
    '''
    
}
