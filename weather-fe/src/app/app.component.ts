import {Component, ElementRef} from '@angular/core';
import {WeatherService} from './weather.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private elRef: ElementRef, private weatherService: WeatherService) { }

  currentImage: string = "";
  searchValue: string= "";
  currentTemp: string= "";
  currentDescription: string= "";

  search() {
    this.weatherService.getData(this.searchValue).subscribe(value => {
      this.currentImage = value.image;
      this.currentDescription = value.description;
      this.currentTemp = value.temp;
      const player = this.elRef.nativeElement.querySelector('video');
      player.load();
    });
  }

}

