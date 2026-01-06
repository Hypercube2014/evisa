import { Component, Injector, OnInit ,Renderer2} from '@angular/core';
import { BaseComponent } from '../../common/commonComponent';
@Component({
  selector: 'app-errorpage',
  templateUrl: './errorpage.component.html',
  styleUrls: ['./errorpage.component.css']
})
export class ErrorpageComponent extends BaseComponent implements OnInit {

  constructor(inj:Injector,private renderer: Renderer2) {
    super(inj)
   }

  ngOnInit(): void {
    this.renderer.addClass(document.body, 'login');
    if (window.opener == null) {
      localStorage.removeItem("evisaCount")
      this.router.navigate(["/home"])
    }
  }

}
