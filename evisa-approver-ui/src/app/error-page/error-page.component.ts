
import { Component, Injector, OnInit ,Renderer2} from '@angular/core';
import { BaseComponent } from '../common/commonComponent';
@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.css']
})
export class ErrorPageComponent extends BaseComponent implements OnInit {

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
