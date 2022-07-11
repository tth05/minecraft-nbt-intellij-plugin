package com.github.tth05.minecraftnbtintellijplugin.editor.ui;

import com.github.tth05.minecraftnbtintellijplugin.NBTTagTreeNode;
import com.github.tth05.minecraftnbtintellijplugin.NBTTagType;
import com.github.tth05.minecraftnbtintellijplugin.util.NBTFileUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

public class NBTFileEditorUI extends JPanel implements DataProvider {

	public static final DataKey<NBTFileEditorUI> DATA_KEY = DataKey.create(NBTFileEditorUI.class.getName());

	private Tree tree;

	private boolean autoSaveEnabled = true;

	public NBTFileEditorUI(@NotNull VirtualFile file, @NotNull Project project) {
		this.setLayout(new BorderLayout());

		//Toolbar
		JPanel northSection = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton saveButton = new JButton("Save", AllIcons.Actions.MenuSaveall);
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				NBTFileUtil.saveTreeToFile(NBTFileEditorUI.this.tree, file, project);
			}
		});

		JBCheckBox autoSaveCheckbox = new JBCheckBox("Save On Change");
		autoSaveCheckbox.setSelected(true);
		autoSaveCheckbox.addItemListener(e -> autoSaveEnabled = e.getStateChange() == ItemEvent.SELECTED);

		northSection.add(saveButton);
		northSection.add(autoSaveCheckbox);

		//Tree Section
		DefaultMutableTreeNode root = NBTFileUtil.loadNBTFileIntoTree(file);
		if (root == null) {
			JBLabel errorText = new JBLabel("Invalid NBT File!");
			errorText.setForeground(JBColor.RED);
			errorText.setHorizontalAlignment(SwingConstants.CENTER);
			this.add(errorText, BorderLayout.CENTER);
			return;
		}

		TreeModel model = new DefaultTreeModel(root);
		//The listener updates the indices in the node names if their parent is some sort of list
		model.addTreeModelListener(new TreeModelListener() {
			@Override
			public void treeNodesChanged(TreeModelEvent e) {
			}

			@Override
			public void treeNodesInserted(TreeModelEvent e) {
			}

			@Override
			public void treeNodesRemoved(TreeModelEvent e) {
				NBTTagTreeNode parent = (NBTTagTreeNode) e.getTreePath().getLastPathComponent();

				if (parent.getChildCount() > 0 && parent.getType() != NBTTagType.COMPOUND) {
					Enumeration<TreeNode> children = parent.children();
					for (int i = 0; children.hasMoreElements(); i++)
						((NBTTagTreeNode) children.nextElement()).setName(i + "");
				}
			}

			@Override
			public void treeStructureChanged(TreeModelEvent e) {
			}
		});

		this.tree = new Tree(model);
		this.tree.setCellRenderer(new NBTFileEditorTreeCellRenderer());
		this.tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					//Find right clicked row and make sure it's selected
					int row = tree.getClosestRowForLocation(e.getX(), e.getY());

					if (!tree.isRowSelected(row))
						tree.setSelectionRow(row);

					ActionPopupMenu menu = ActionManager.getInstance().createActionPopupMenu(ActionPlaces.POPUP,
							(ActionGroup) ActionManager.getInstance().getAction(
									"com.github.tth05.minecraftnbtintellijplugin.actions.NBTFileEditorPopupGroup"));
					menu.setTargetComponent(NBTFileEditorUI.this);
					menu.getComponent().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		this.add(northSection, BorderLayout.NORTH);
		this.add(new JBScrollPane(this.tree), BorderLayout.CENTER);
	}

	public Tree getTree() {
		return this.tree;
	}

	@Nullable
	@Override
	public Object getData(@NotNull String dataId) {
		if (DATA_KEY.is(dataId))
			return this;
		return null;
	}

	public boolean isAutoSaveEnabled() {
		return autoSaveEnabled;
	}
}
