package com.github.tth05.minecraftnbtintellijplugin.editor.ui;

import com.github.tth05.minecraftnbtintellijplugin.util.NBTParserUtil;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NBTFileEditorUI extends JPanel implements DataProvider {

	private final Tree tree;
	public static final DataKey<NBTFileEditorUI> DATA_KEY = DataKey.create(NBTFileEditorUI.class.getName());

	public NBTFileEditorUI(@NotNull VirtualFile file) {
		this.setLayout(new BorderLayout());

		DefaultMutableTreeNode root = NBTParserUtil.loadNBTFileIntoTree(file);

		TreeModel model = new DefaultTreeModel(root);
		this.tree = new Tree(model);
		this.tree.setCellRenderer(new NBTFileEditorTreeCellRenderer());
		this.tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					//Find right clicked row and make sure it's selected
					int row = tree.getClosestRowForLocation(e.getX(), e.getY());

					if (!tree.isRowSelected(row)) {
						tree.setSelectionRow(row);
						//Request focus to make the tree instantly update after something has changed by performing an
						// action. Without focus the tree wouldn't repaint
						NBTFileEditorUI.this.requestFocus();
					}

					ActionPopupMenu menu = ActionManager.getInstance().createActionPopupMenu(ActionPlaces.POPUP,
							(ActionGroup) ActionManager.getInstance().getAction(
									"com.github.tth05.minecraftnbtintellijplugin.actions.NBTFileEditorPopupGroup"));
					menu.setTargetComponent(NBTFileEditorUI.this);
					menu.getComponent().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		JBScrollPane scrollPane = new JBScrollPane(this.tree);

		this.add(scrollPane);
	}

	@NotNull
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
}
