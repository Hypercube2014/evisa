import { Component, OnInit, Injector } from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
declare var $: any;
@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent extends BaseComponent implements OnInit {

  public base64Image = 'data:image/png;base64,'
  constructor(inj: Injector) {
    super(inj)
  }

  ngOnInit(): void {

    $('#click').click(function () {
      $("#panel").toggle();
      $("#text").toggle();
      $('[aria-label="footer"]').toggle();
      $('#arrow').toggleClass('fa-arrow-left')
      $('#arrow').toggleClass('fa-arrow-right')
    });


    var selector = '.nav li';

    $(selector).on('click', function () {
      $(selector).removeClass('active');
      $(this).addClass('active');
    });

    this.getRoles()



    $('.page-sidebar').on('click', 'li > a', function (e) {
      // if ($(this).next().hasClass('sub-menu') == false) {
      // if ($('.btn-navbar').hasClass('collapsed') == false) {
      // $('.btn-navbar').click();
      // }
      // return;
      // }

      // if ($(this).next().hasClass('sub-menu.always-open')) {
      //     return;
      // }




      var parent = $(this).parent().parent();
      var the = $(this);

      parent.children('li.open').children('a').children('.arrow').removeClass('open');
      parent.children('li.open').children('.sub-menu').slideUp(200);
      parent.children('li.open').removeClass('open');

      var sub = $(this).next();
      var slideOffeset = -200;
      var slideSpeed = 200;

      if (sub.is(":visible")) {
        $('.arrow', $(this)).removeClass("open");
        $(this).parent().removeClass("open");
        sub.slideUp(slideSpeed, function () {
          if ($('body').hasClass('page-sidebar-fixed') == false && $('body').hasClass('page-sidebar-closed') == false) {
            // App.scrollTo(the, slideOffeset);
          }
          handleSidebarAndContentHeight();
        });
      } else {
        $('.arrow', $(this)).addClass("open");
        $(this).parent().addClass("open");
        sub.slideDown(slideSpeed, function () {
          if ($('body').hasClass('page-sidebar-fixed') == false && $('body').hasClass('page-sidebar-closed') == false) {
            // App.scrollTo(the, slideOffeset);
          }
          handleSidebarAndContentHeight();
        });
      }

      e.preventDefault();
    });

    // handle ajax links
    $('.page-sidebar').on('click', ' li > a.ajaxify', function (e) {
      e.preventDefault();
      // App.scrollTop();

      var url = $(this).attr("href");
      var menuContainer = $('.page-sidebar ul');
      var pageContent = $('.page-content');
      var pageContentBody = $('.page-content .page-content-body');

      menuContainer.children('li.active').removeClass('active');
      menuContainer.children('arrow.open').removeClass('open');

      $(this).parents('li').each(function () {
        $(this).addClass('active');
        $(this).children('a > span.arrow').addClass('open');
      });
      $(this).parents('li').addClass('active');


    });




    var handleSidebarAndContentHeight = function () {
      var content = $('.page-content');
      var sidebar = $('.page-sidebar');
      var body = $('body');
      var height;

      if (body.hasClass("page-footer-fixed") === true && body.hasClass("page-sidebar-fixed") === false) {
        var available_height = $(window).height() - $('.footer').outerHeight();
        if (content.height() < available_height) {
          content.attr('style', 'min-height:' + available_height + 'px !important');
        }
      } else {
        if (body.hasClass('page-sidebar-fixed')) {
          height = _calculateFixedSidebarViewportHeight();
        } else {
          height = sidebar.height() + 20;
        }
        if (height >= content.height()) {
          content.attr('style', 'min-height:' + height + 'px !important');
        }
      }
    }
    // Helper function to calculate sidebar height for fixed sidebar layout.
    var _calculateFixedSidebarViewportHeight = function () {
      var sidebarHeight = $(window).height() - $('.header').height() + 1;
      if ($('body').hasClass("page-footer-fixed")) {
        sidebarHeight = sidebarHeight - $('.footer').outerHeight();
      }

      return sidebarHeight;
    }

  }



  ngAfterViewInit() {

    $('.page-sidebar, .header').on('click', '.sidebar-toggler', function (e) {
      //console.log("called");
      var body = $('body');
      var sidebar = $('.page-sidebar');
      //console.log(body.hasClass("page-sidebar-closed"))
      //console.log(body.hasClass("page-sidebar-fixed"));
      //console.log(body.hasClass("page-sidebar-hover-on"), body.hasClass('page-sidebar-fixed'), sidebar.hasClass('page-sidebar-hovering'));
      if ((body.hasClass("page-sidebar-hover-on") && body.hasClass('page-sidebar-fixed')) || sidebar.hasClass('page-sidebar-hovering')) {

        //console.log("inside")
        body.removeClass('page-sidebar-hover-on');
        sidebar.css('width', '').hide().show();
        //$.cookie('sidebar_closed', '0');
        e.stopPropagation();
        //runResponsiveHandlers();
        return;
      }

      $(".sidebar-search", sidebar).removeClass("open");

      if (body.hasClass("page-sidebar-closed")) {
        body.removeClass("page-sidebar-closed");
        if (body.hasClass('page-sidebar-fixed')) {
          sidebar.css('width', '');
        }
        // $.cookie('sidebar_closed', '0');
      } else {
        body.addClass("page-sidebar-closed");
        // $.cookie('sidebar_closed', '1');
      }

      $('.page-sidebar').on('click', 'li > a', function (e) {
        if ($(this).next().hasClass('sub-menu') == false) {
          if ($('.btn-navbar').hasClass('collapsed') == false) {
            $('.btn-navbar').click();
          }
          return;
        }
      })

    });

  }

}
