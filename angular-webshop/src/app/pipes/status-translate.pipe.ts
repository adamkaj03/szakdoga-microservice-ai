import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'statusTranslate'
})
export class StatusTranslatePipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    switch (value) {
      case 'NEW':
        return 'Új';
      case 'COMPLETED':
        return 'Végrehajtott';
      case 'FAILED':
        return 'Sikertelen';
      default:
        return value;
    }
  }

}
