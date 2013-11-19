package com.excelsh.core.menu;

import com.excelsh.core.common.GlobalConst;
import java.util.Vector;

public class MenuItem {
  private Vector vSubMenuItem = null;

  private String strMenuID = GlobalConst.CONST_STRING_EMPTY;
  private String strMenuLink = GlobalConst.CONST_STRING_EMPTY;
  private String strMenuLinkPrefix = GlobalConst.CONST_STRING_EMPTY;
  private String strMenuDesc = GlobalConst.CONST_STRING_EMPTY;
  private int intMenuLevel;
  private String strMenuSecurity = GlobalConst.CONST_STRING_EMPTY;

  // Added by Helen 2009/02/03 for # Start
  private int intSubMenuMaxLength = 0;
  // Added by Helen 2009/02/03 for # End
  
  public MenuItem()
  {
    vSubMenuItem = new Vector();
  }

  public String getMenuID()                 {    return this.strMenuID;  }
  public void setMenuID(String straMenuID)  {    this.strMenuID = straMenuID;  }

  public String getMenuLink()                   {   return this.strMenuLink;  }
  public void setMenuLink(String straMenuLink)  {   this.strMenuLink = straMenuLink;}

  public String getMenuLinkPrefix()                       {   return this.strMenuLinkPrefix;  }
  public void setMenuLinkPrefix(String straMenuLinkPrefix)  {   this.strMenuLinkPrefix = straMenuLinkPrefix;}

  public String getMenuDesc()                   {   return this.strMenuDesc;  }
  public void setMenuDesc(String straMenuDesc)  {   this.strMenuDesc = straMenuDesc;}

  public int getMenuLevel()                    {   return this.intMenuLevel;  }
  public void setMenuLevel(int intaMenuLevel)  {   this.intMenuLevel = intaMenuLevel;}

  public String getMenuSecurity()                    {   return this.strMenuSecurity;  }
  public void setMenuSecurity(String straMenuSecurity)  {   this.strMenuSecurity = straMenuSecurity;}

  public int getSubMenuLength()   {    return this.vSubMenuItem.size();  }

  // Added by Helen 2009/02/03 for # Start
  public int getSubMenuMaxLength(){
	  return this.intSubMenuMaxLength;
  }
  public void setSubMenuMaxLength(int length){
	  this.intSubMenuMaxLength = length;
  }
  // Added by Helen 2009/02/03 for # End
  public void addSubMenuItem(MenuItem miaSubMenuItem)
  {
    this.vSubMenuItem.add(miaSubMenuItem);
  }

  public Vector getSubMenuItem()
  {
    return this.vSubMenuItem;
  }

  public void setSubMenuItem(Vector vaSubMenuItem)
  {
    this.vSubMenuItem = (Vector)vaSubMenuItem.clone();
  }

}