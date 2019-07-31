package com.github.tth05.minecraftnbtintellijplugin.util;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.JBColor;
import com.intellij.ui.SimpleListCellRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class ChooseTypeDialog extends DialogWrapper {

	private final ComboBox<NBTTagType> comboBox;

	public ChooseTypeDialog(@Nullable Project project, @NotNull NBTTagType selectedType) {
		super(project, true);
		setTitle("Choose Type");

		if (!SystemInfo.isMac) {
			setButtonsAlignment(SwingConstants.CENTER);
		}

		this.comboBox = new ComboBox<>(NBTTagType.values());
		this.comboBox.setSelectedItem(selectedType);
		this.comboBox.setRenderer(new SimpleListCellRenderer<NBTTagType>() {
			@Override
			public void customize(JList<? extends NBTTagType> list, NBTTagType value, int index, boolean selected,
			                      boolean hasFocus) {
				setText(value.name());
				setIcon(value.getIcon());
			}
		});

		init();
	}

	@Nullable
	@Override
	protected JComponent createCenterPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(330, getPreferredSize().height));

		JLabel label = new JLabel("Choose the new type for this node:");
		label.setBorder(BorderFactory.createEmptyBorder(0, 2, 5, 0));

		JLabel warningLabel = new JLabel("All children will be deleted if you change the type of a node!");
		warningLabel.setForeground(JBColor.ORANGE);
		warningLabel.setHorizontalAlignment(JLabel.CENTER);

		panel.add(label, BorderLayout.NORTH);
		panel.add(this.comboBox, BorderLayout.CENTER);
		panel.add(warningLabel, BorderLayout.SOUTH);

		return panel;
	}

	public NBTTagType getResult() {
		return (NBTTagType) this.comboBox.getSelectedItem();
	}
}