package pl.mateam.marpg.core.objects.worlds.tectonic;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlate;

public abstract class AbstractTectonicPlateManager<T> {
	protected final TectonicPlate parentPlate;
	protected T parent;
	
	private final Function<TectonicPlate, T> originalParentSupplier;
	
	public AbstractTectonicPlateManager(TectonicPlate parentPlate, Function<TectonicPlate, T> parentSupplier) {
		this.parentPlate = parentPlate;
		this.originalParentSupplier = parentSupplier;
		this.parent = originalParentSupplier.apply(parentPlate);
	}
	
	protected final void updateSubplates(Consumer<TectonicPlate> updateAction) {
		for(TectonicPlate tectonicPlate : ((TectonicPlateImplementation) parentPlate).getAllSubplates())
			updateAction.accept(tectonicPlate);
	}
	
	protected final <X> X getValue(X fieldValue, Supplier<X> methodToInvoke, X coreValue){
		if(fieldValue != null)
			return fieldValue;
		else
			return getParentValue(methodToInvoke, coreValue);
	}
	
	protected final <X> X getParentValue(Supplier<X> methodToInvoke, X coreValue) {
		if(parent == null)
			return coreValue;
		return methodToInvoke.get();
	}
	
	
	protected void setParentController(T parent) 	{	this.parent = parent;											}
	protected void restoreNaturalParent() 			{	this.parent = originalParentSupplier.apply(parentPlate);		}
}
