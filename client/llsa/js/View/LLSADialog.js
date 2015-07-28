define([
           'dojo/_base/declare',
           'dojo/dom-construct',
           'dijit/focus',
           'dijit/form/TextBox',
           'dojo/on',
           'dojo/request/xhr',
           'dijit/form/Button',
           'JBrowse/View/Dialog/WithActionBar',
       ],
       function( declare, dom, focus, dijitTextBox, on, xhr, Button, ActionBarDialog ) {


return declare( ActionBarDialog,

    /**
     * Dijit Dialog subclass that pops up prompt for the user for loci information
     * @lends JBrowse.View.InfoDialog
     */
{
    autofocus: false,
    title: 'Create alternative loci',

    constructor: function( args ) {
        this.browser = args.browser;
        this.setCallback    = args.setCallback || function() {};
        this.cancelCallback = args.cancelCallback || function() {};
    },

    _fillActionBar: function( actionBar ) {
        var thisB = this;
        new Button({ iconClass: 'dijitIconDelete', label: 'Cancel',
                     onClick: function() {
                         thisB.cancelCallback && thisB.cancelCallback();
                         thisB.hide();
                     }
                   })
            .placeAt( actionBar );
        new Button({ iconClass: 'dijitIconFilter',
                     label: 'Create alteration',
                     onClick:function() {
                         xhr.post('/apollo/alternativeLoci/addLoci', {
                             data: {
                                 start: thisB.start.get('value'),
                                 end: thisB.end.get('value'),
                                 name: thisB.lociName.get('value')
                             }
                         }).then(
                             function() { console.log("success"); thisB.browser.view.redrawTracks(); },
                             function() { console.log("error"); }
                         );
                         thisB.hide();
                     }
                   })
            .placeAt( actionBar );
    },

    show: function( callback ) {
        var thisB = this;

        dojo.addClass( this.domNode, 'setLLSA' );

        var visibleLocation = "";

        this.lociName = new dijitTextBox({
            id: 'llsa_name',
            value: ""
        });

        this.start = new dijitTextBox({
            id: 'llsa_start',
            value: "" 
        });

        this.end = new dijitTextBox({
            id: 'llsa_end',
            value: ""
        });

        this.set('content', [
                     dom.create('label', { "for": 'llsa_name', innerHTML: 'Name: ' } ),
                     this.lociName.domNode,
                     dom.create('br'),
                     dom.create('label', { "for": 'llsa_start', innerHTML: 'Start: ' } ),
                     this.start.domNode,
                     dom.create('br'),
                     dom.create('label', { "for": 'llsa_end', innerHTML: 'End: ' } ),
                     this.end.domNode
                 ] );



        this.inherited( arguments );
    },

    hide: function() {
        this.inherited(arguments);
        window.setTimeout( dojo.hitch( this, 'destroyRecursive' ), 500 );
    }
});
});
