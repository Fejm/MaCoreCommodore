package pl.mateam.marpg.core.modules.external.database.explorer;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.DatabaseReader;

public abstract class AbstractDatabaseReader implements DatabaseReader {	
	protected final Document document;
	public AbstractDatabaseReader(Object document) {
		if(document != null && document instanceof Document)
			this.document = (Document) document;
		else this.document = null;
	}
	
	
	@Override public final boolean isValid()	{	return document != null;	}
}
