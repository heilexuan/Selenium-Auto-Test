package com.excelsh.core.menu;

import com.excelsh.core.common.SeleniumUtil;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;


public class MenuCache {
	private Vector vecMenuItems = null;
	
	public MenuCache(String straRealPath, boolean baByPassValidation, String strLangId){
		String strXMLMenuPath = SeleniumUtil.getPropManager().getPropMain().getProperty("MENUITEMS.XML", "");		
		vecMenuItems = getMenuItem(straRealPath, baByPassValidation, strLangId, strXMLMenuPath);
	}
	public MenuCache(String straRealPath, boolean baByPassValidation, String strLangId, String strMenuFileName){
		vecMenuItems = getMenuItem(straRealPath, baByPassValidation, strLangId, strMenuFileName);
	}	
	private static Vector getMenuItem(String straRealPath, boolean bByPassValidation, String strLangId, String strMenuFileName){		
		com.excelsh.core.menu.MenuReader menuReader = new com.excelsh.core.menu.MenuReader();
		String strLangID = "1";
		String strXMLRootPath = SeleniumUtil.getPropManager().getPropMain().getProperty("ROOT_PATH.XML", "");;	
		String strXMLMenuPath = strXMLRootPath + strMenuFileName;
		
		menuReader.readFile(strXMLMenuPath, !bByPassValidation);
		
		com.excelsh.core.menu.MenuItem aMenuItem = menuReader.getMenuItem();
		java.util.Vector vSubMenuItem = aMenuItem.getSubMenuItem();	
		return vSubMenuItem;		
	}	
	public String getInitMenuScript(){
		MenuItem menuItem = null;
		StringBuffer strOut = new StringBuffer(2000);		
		int iSize = vecMenuItems.size();
		for (int i=0; i<iSize; i++){
			menuItem = (MenuItem)vecMenuItems.get(i);			
			strOut.append("appendNode('null','").append(menuItem.getMenuID()).append("','").
				append(menuItem.getMenuDesc()).append("','','group','','');");
			recursionSubMenuScript(menuItem, strOut);
		}
		return strOut.toString();
	}
	private void recursionSubMenuScript(MenuItem menuItem, StringBuffer strOut){		
		if (menuItem.getSubMenuLength() > 0){		
			String strSeparator = "";
			MenuItem subMenu = null;
			Vector vecMenuItems = menuItem.getSubMenuItem();			
			int iSize = vecMenuItems.size();
			for (int i=0; i<iSize; i++){
				subMenu = (MenuItem)vecMenuItems.get(i);
				if (subMenu.getSubMenuLength() > 0){					
					strOut.append("appendNode('").append(menuItem.getMenuID()).append("','").append(subMenu.getMenuID()).append("','").
					append(subMenu.getMenuDesc()).append("','','group','','');");
					recursionSubMenuScript(subMenu, strOut);
				} else {
					strSeparator = (subMenu.getMenuLink().indexOf("?")>=0?"&":"?");
					strOut.append("appendLeaf('").append(menuItem.getMenuID()).append("','").append(subMenu.getMenuID()).append("','").append(subMenu.getMenuDesc()).append("','','")
					.append(subMenu.getMenuLinkPrefix()).append("','").append(subMenu.getMenuLink()).append(strSeparator)
					.append("MENU_ID=").append(subMenu.getMenuID()).append("');");
				}				
			}
		}		 
	}
	public String getTopMenuHtml(){
		MenuItem menuItem = null;
		StringBuffer strOut = new StringBuffer(2000);
		strOut.append("<table class='menu_top' cellpadding='0' cellspacing='0'><tr>");
		int iSize = vecMenuItems.size();
		for (int i=0; i<iSize; i++){
			menuItem = (MenuItem)vecMenuItems.get(i);
			strOut.append("<td class='menu_top' nowrap onMouseOver='showMenu(\"").append(menuItem.getMenuID())
				.append("\",getIEPosX(this),0)'>&nbsp;&nbsp;")				
				.append(menuItem.getMenuDesc()).append("&nbsp;&nbsp;</td>");			
		}
		strOut.append("</tr></table>");
		return strOut.toString();
	}
	
	public MenuItem getMenuItemByDesc(Vector<MenuItem> vecMenuItems, String menuDesc){
		MenuItem menuItem = null;
		int iSize = vecMenuItems.size();
		for (int i=0; i<iSize; i++){
			menuItem = (MenuItem)vecMenuItems.get(i);
			if(menuItem.getMenuDesc().equals(menuDesc)){
				break;
			}
		}
		return menuItem;
	}
	
	public MenuItem getMenuItemByDesc(String firstMenu, String secondMenu, String thirdMenu){
		MenuItem menuItem = null;
		int iSize = vecMenuItems.size();
		if(!StringUtils.isEmpty(firstMenu)){
			menuItem = this.getMenuItemByDesc(vecMenuItems, firstMenu);
			if(!StringUtils.isEmpty(secondMenu)){
				menuItem = this.getMenuItemByDesc(menuItem.getSubMenuItem(), secondMenu);
				if(!StringUtils.isEmpty(thirdMenu)){
					menuItem = this.getMenuItemByDesc(menuItem.getSubMenuItem(), thirdMenu);
				}
			}
		}
		
		return menuItem;
	}
	/***
	 * 
	 * @param htOnlyShowMenu: if htOnlyShowMenu is null, mean show all menus, otherwise only show the menu which contain in the htOnlyShowMenu
	 * @return
	 */
	public String getLeftPanelMenuHtml(Hashtable htOnlyShowMenu){
		MenuItem menuItem = null;
		StringBuffer strOut = new StringBuffer(2000);
		//Start #24855, Leo Lu, 2010/09/13
		int strMenuLvl;
		String strSubEachLvl = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		//End #24855, Leo Lu, 2010/09/13
		strOut.append("<table width=\"90%\" class='gridrmdata' cellpadding='0' cellspacing='0'>");
		int iSize = vecMenuItems.size();
		for (int i=0; i<iSize; i++){
			menuItem = (MenuItem)vecMenuItems.get(i);
			strMenuLvl = menuItem.getMenuLevel();
			if (htOnlyShowMenu == null || htOnlyShowMenu.containsKey(menuItem.getMenuID())){
				if ("LINE".equals(menuItem.getMenuDesc())){
					strOut.append("<tr height='3'>");
					strOut.append("<td></td></tr>");
					strOut.append("<tr height='1' style='BACKGROUND-COLOR: #C0C0C0'>");
					strOut.append("<td></td></tr>");
					strOut.append("<tr height='3'>");
					strOut.append("<td></td></tr>");
				} else {
					strOut.append("<tr class=\"submenuitem1\">");
					strOut.append("<td>");
					if(strMenuLvl == 3) {
						strOut.append(strSubEachLvl);
					}
					strOut.append("<img src=\"images/leftmenu_tri.gif\" border=\"0\">&nbsp;&nbsp;");
					strOut.append("<a href=\"javascript:loadpage('");
					strOut.append(menuItem.getMenuLinkPrefix()).append("','");
					strOut.append(menuItem.getMenuLink()).append("')\">");
					strOut.append(menuItem.getMenuDesc()).append("</a></td>");
					strOut.append("</tr>");
				}
			}
		}
		strOut.append("</table>");
		return strOut.toString();
	}
}
