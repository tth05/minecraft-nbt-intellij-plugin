package com.github.tth05.minecraftnbtintellijplugin;

import javax.swing.tree.DefaultMutableTreeNode;

public class NBTValueTreeNode extends DefaultMutableTreeNode {

	private NBTTagType type;
	private String name;
	private Object value;

	public NBTValueTreeNode(NBTTagType type, String name, Object value) {
		super(name + ": " + value.toString());
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public void update() {
		setUserObject(this.name + ": " + getValueAsString());
	}


	public NBTTagType getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public void setType(NBTTagType type) {
		this.type = type;
		setValue(type.getDefaultValue());
	}

	public void setName(String name) {
		this.name = name;
		update();
	}

	public void setValue(Object value) {
		this.value = value;
		update();
	}

	public String getValueAsString() {
		String stringValue;
		if(this.value == null)
			stringValue = "";
		else if(this.value instanceof String)
			stringValue = "\"" + this.value.toString() + "\"";
		else
			stringValue = this.value.toString();
		return stringValue;
	}
}
