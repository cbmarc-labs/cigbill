package cbmarc.cigbill.client.utils;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;

public interface EditorView<T> extends Editor<T> {
	
	SimpleBeanEditorDriver<T, ?> createEditorDriver();

}
