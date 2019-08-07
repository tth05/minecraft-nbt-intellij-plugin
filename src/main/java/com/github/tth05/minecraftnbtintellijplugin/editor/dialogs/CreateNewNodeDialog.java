package com.github.tth05.minecraftnbtintellijplugin.editor.dialogs;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.SimpleListCellRenderer;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.panels.VerticalBox;
import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

//TODO: Fix weird bug where error makes the Name JTextField invisible
public class CreateNewNodeDialog extends DialogWrapper {

	private final ComboBox<NBTTagType> comboBox;
	private final String name;
	private JBTextField textField;

	public CreateNewNodeDialog(@Nullable Project project, @Nullable String name) {
		super(project, true);
		this.name = name;
		setTitle("Create New");

		if (!SystemInfo.isMac) {
			setButtonsAlignment(SwingConstants.CENTER);
		}

		this.comboBox = new ComboBox<>(NBTTagType.values());
		this.comboBox.setRenderer(new SimpleListCellRenderer<NBTTagType>() {
			@Override
			public void customize(JList<? extends NBTTagType> list, NBTTagType value, int index, boolean selected,
			                      boolean hasFocus) {
				setText(value.name());
				setIcon(value.getIcon());
			}
		});

		if (name == null)
			this.textField = new JBTextField(150);

		init();
	}

	@Nullable
	@Override
	protected JComponent createCenterPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(330, getPreferredSize().height));

		VerticalBox box = new VerticalBox();

		JBLabel typeLabel = new JBLabel("Type:");
		typeLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 5, 0));

		box.add(typeLabel);
		box.add(this.comboBox);

		if (name == null) {
			JBLabel nameLabel = new JBLabel("Name:");

			nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 0));
			box.add(nameLabel);
			box.add(this.textField);
		}

		panel.add(box, BorderLayout.CENTER);
		return panel;
	}

	public NBTTagType getType() {
		return (NBTTagType) this.comboBox.getSelectedItem();
	}

	public String getName() {
		return name == null ?
				this.textField.getText() :
				name;
	}

	@Nullable
	@Override
	protected ValidationInfo doValidate() {
		if (this.textField == null)
			return null;

		if (this.textField.getText() == null || this.textField.getText().trim().isEmpty())
			return new ValidationInfo("Invalid name!");

		return null;
	}
}