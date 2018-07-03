package pl.mateam.marpg.core.modules.external.database.explorer;

import java.util.function.Function;

import org.bson.Document;

public abstract class AbstractDatabaseWriter {
	protected final Document document;
	public AbstractDatabaseWriter(Document document) 	{	this.document = document;	}	//Document to write on. On subwriters, nested document should be created and put onto main one!
	
	protected final <T extends AbstractDatabaseWriter> T getSubWriter(Function<Document, T> constructor, String subwriterFieldName){
		Object object = document.get(subwriterFieldName);
		if(object != null && object instanceof Document)
			return constructor.apply((Document) object);
		else {
			Document newDocument = new Document();
			document.put(subwriterFieldName, newDocument);
			return constructor.apply(newDocument);
		}
	}
}
