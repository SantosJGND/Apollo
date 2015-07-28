define([
           'dojo/_base/declare',
           'dojo/_base/lang',
           'JBrowse/Plugin',
           'dijit/MenuItem',
           'dijit/registry',
           'LLSA/View/LLSADialog'
       ],
       function(
           declare,
           lang,
           JBrowsePlugin,
           dijitMenuItem,
           dijitRegistry,
           LLSADialog
       ) {
return declare( JBrowsePlugin,
{
    constructor: function( args ) {
        var browser = args.browser;
        var thisB = this;
        console.log( "LLSA plugin starting" );
        browser.afterMilestone('completely initialized', function () {
            console.log("LLSA initView");
            // do anything you need to initialize your plugin here
            var buttontext = new dijitMenuItem({
              label: 'Create LLSA',
              iconClass: 'dijitIconBookmark',
              onClick: lang.hitch(thisB, 'createLLSA')
            });
            
            
            if(!dijitRegistry.byId("dropdownmenu_tools")){
                 browser.renderGlobalMenu('tools',{text:'Tools'},browser.menuBar);
            }
            browser.addGlobalMenuItem('tools',buttontext);
        });
    },
    createLLSA: function () {
      var dialog = new LLSADialog({browser:this.browser});
      dialog.show(function() { console.log("Callback"); });
    }
});
});
