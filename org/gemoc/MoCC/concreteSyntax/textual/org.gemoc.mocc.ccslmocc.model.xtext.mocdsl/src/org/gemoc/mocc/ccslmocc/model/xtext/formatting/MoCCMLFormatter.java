/**
 * Copyright (c) 2012-2016 GEMOC consortium.
 * 
 * http://www.gemoc.org
 * 
 * Contributors:
 * *   
 *   P. Issa Diallo - ENSTA Bretagne [papa_issa.diallo@ensta-bretagne.fr]
 *   Stephen Creff - ENSTA Bretagne [stephen.creff@ensta-bretagne.fr] 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * $Id$
 */
/*
 * generated by Xtext
 */
package org.gemoc.mocc.ccslmocc.model.xtext.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.util.Pair;

/**
 * This class contains custom formatting description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation.html#formatting
 * on how and when to use it 
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
public class MoCCMLFormatter extends AbstractDeclarativeFormatter {
	
	@Override
	protected void configureFormatting(FormattingConfig c) {
		org.gemoc.mocc.ccslmocc.model.xtext.services.MoCDslGrammarAccess f = (org.gemoc.mocc.ccslmocc.model.xtext.services.MoCDslGrammarAccess) getGrammarAccess();
		for(Pair<Keyword, Keyword> pair: f.findKeywordPairs("{", "}")) {
			c.setIndentation(pair.getFirst(), pair.getSecond());
			c.setLinewrap(1).after(pair.getFirst());
			c.setLinewrap(1).before(pair.getSecond());
			c.setLinewrap(1).after(pair.getSecond());
		}
		
		for(Keyword comma: f.findKeywords(",")) {
			c.setNoLinewrap().before(comma);
			c.setNoSpace().before(comma);
			//c.setLinewrap().after(comma);
		}
		
		for(Keyword dotcomma: f.findKeywords(";")) {
			c.setLinewrap(1).after(dotcomma);
		}
		
		for(Keyword autodef: f.findKeywords("AutomataRelationDefinition")) {
			c.setLinewrap(2).before(autodef);
		}
		
		for(Keyword init: f.findKeywords("init: ")) {
			c.setLinewrap(2).before(init);
		}
		
		for(Keyword init: f.findKeywords("finals: ")) {
			c.setLinewrap(2).before(init);
		}
		
		for(Keyword from: f.findKeywords("from")) {
			c.setLinewrap(2).before(from);
		}
		
		for(Keyword guard: f.findKeywords("guards {")) {
			c.setLinewrap(1).before(guard);
		}
		
		for(Keyword vars: f.findKeywords("variables {")) {
			c.setLinewrap(2).before(vars);
		}
		
		for(Keyword state: f.findKeywords("State")) {
			c.setLinewrap(2).before(state);
		}
		
		for(Keyword point: f.findKeywords(".")) {
			c.setNoLinewrap().before(point);
			c.setNoSpace().before(point);
			c.setNoSpace().after(point);
			c.setLinewrap().after(point);
		}
		
		c.setLinewrap(0, 1, 2).before(f.getSL_COMMENTRule());
		c.setLinewrap(0, 1, 2).before(f.getML_COMMENTRule());
		c.setLinewrap(0, 1, 1).after(f.getML_COMMENTRule());
	}
}
