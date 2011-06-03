package org.zkoss.fiddle.component.renderer;

import org.zkoss.codemirror.CodeEditor;
import org.zkoss.fiddle.component.Texttab;
import org.zkoss.fiddle.composer.event.FiddleEventQueues;
import org.zkoss.fiddle.composer.event.FiddleEvents;
import org.zkoss.fiddle.composer.event.SourceRemoveEvent;
import org.zkoss.fiddle.model.api.IResource;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Tab;
import org.zkoss.zul.api.Tabpanels;
import org.zkoss.zul.api.Tabs;

public class SourceTabRenderer implements ISourceTabRenderer {

	protected CodeEditor prepareCodeEditor(final IResource resource) {
		CodeEditor ce = new CodeEditor();
		ce.setMode(resource.getTypeMode());
		ce.setValue(resource.getContent());
		ce.setHeight("400px");
		ce.setWidth("auto");
		ce.setAttribute("model", resource);

		ce.addEventListener(Events.ON_CHANGE, new EventListener() {

			public void onEvent(Event event) throws Exception {

				InputEvent inpEvt = (InputEvent) event;
				CodeEditor ce = (CodeEditor) event.getTarget();
				IResource r = (IResource) ce.getAttribute("model");
				r.setContent(inpEvt.getValue());
			}
		});
		return ce;
	}

	protected Tab renderTab(final IResource resource) {
		// TODO using swifttab to replace this if possible
		/* creating tab */
		Texttab texttab = new Texttab(resource.getTypeName());
		texttab.setAttribute("model", resource);

		final Textbox box = new Textbox(resource.getName());
		box.setSclass("tab-textbox");
		box.setConstraint("no empty");
		box.setInplace(true);
		box.setDisabled(!resource.isCanDelete());

		texttab.appendChild(box);
		texttab.setClosable(resource.isCanDelete());

		box.addEventListener("onChange", new EventListener() {

			public void onEvent(Event event) throws Exception {
				resource.setName(box.getValue());
			}
		});

		texttab.addEventListener("onClose", new EventListener() {

			public void onEvent(Event event) throws Exception {
				Texttab tab = (Texttab) event.getTarget();
				EventQueue queue = EventQueues.lookup(FiddleEventQueues.SOURCE, true);
				queue.publish(new SourceRemoveEvent(FiddleEvents.ON_SOURCE_REMOVE, null, (IResource) tab
						.getAttribute("model")));
			}
		});

		return texttab;
	}

	protected Tabpanel renderTabpanel(IResource resource) {

		/* creating Tabpanel */
		Tabpanel sourcepanel = new Tabpanel();

		sourcepanel.appendChild(prepareCodeEditor(resource));

		return sourcepanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.zkoss.fiddle.component.renderer.ISourceTabRenderer#appendSourceTab
	 * (org.zkoss.zul.api.Tabs, org.zkoss.zul.api.Tabpanels,
	 * org.zkoss.fiddle.model.api.IResource)
	 */
	public void appendSourceTab(Tabs sourcetabs, Tabpanels sourcetabpanels, final IResource resource) {
		if (sourcetabs == null || sourcetabpanels == null) {
			throw new IllegalStateException("sourcetabpanels/sourcetabs is not ready !!\n"
					+ " do you call this after doAfterCompose "
					+ "(and be sure you called super.doAfterCompose()) or using in wrong page? ");
		}
		sourcetabs.appendChild(renderTab(resource));
		sourcetabpanels.appendChild(renderTabpanel(resource));
	}
}
