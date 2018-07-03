package pl.mateam.marpg.api.regular.objects.worlds.tectonic;

public interface TectonicPlateModule<T> {
	void setParentController(T parentController);
	void restoreNaturalParent();
}
