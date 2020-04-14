import logging

import requests

from sample_images import IMG_DIR
from sample_images.annotations import name_2_annotation, SAMPLE_IMAGES
from src.constants import ENV
from src.exceptions import NoFaceFoundError
from src.init_runtime import init_runtime
from src.services.dto.scanned_face import ScannedFace
from src.services.facescan.scanner.facescanner import FaceScanner
from src.services.facescan.scanner.facescanners import id_2_face_scanner_cls
from src.services.facescan.scanner.test.calculate_errors import calculate_errors
from src.services.facescan.visualizer.show import show_img
from src.services.imgtools.read_img import read_img
from src.services.utils.pyutils import get_env, Constants


class _ENV(Constants):
    USE_REMOTE = get_env('USE_REMOTE', 'false').lower() in ('true', '1')
    ML_HOST = get_env('ML_HOST', 'localhost')
    ML_PORT = int(get_env('ML_PORT', '3000'))
    ML_URL = get_env('ML_URL', f'http://{ML_HOST}:{ML_PORT}')
    IMG_NAMES = Constants.split(get_env('IMG_NAMES', ' '.join([i.img_name for i in SAMPLE_IMAGES])))
    _SHOW_IMG_VAL = get_env('SHOW_IMG', 'true').lower()
    SHOW_IMG = _SHOW_IMG_VAL in ('true', '1')
    SHOW_IMG_ON_ERROR = _SHOW_IMG_VAL == 'on_error'


def _scan_faces_remote(ml_url: str, img_name: str):
    files = {'file': open(IMG_DIR / img_name, 'rb')}
    res = requests.post(f"{ml_url}/scan_faces", files=files)
    if res.status_code == 400 and res.json()['message'] == NoFaceFoundError.description:
        return []
    assert res.status_code == 200, res.content
    return [ScannedFace.from_request(r) for r in res.json()['result']]


def _scan_faces_local(scanner_id, img_name):
    img = read_img(IMG_DIR / img_name)
    scanner: FaceScanner = id_2_face_scanner_cls[scanner_id]()
    return scanner.scan(img)


def _scan_faces(img_name: str):
    if _ENV.USE_REMOTE:
        return _scan_faces_remote(_ENV.ML_URL, img_name)
    else:
        return _scan_faces_local(ENV.SCANNER, img_name)


if __name__ == '__main__':
    init_runtime(logging_level=logging.DEBUG)
    logging.debug(_ENV.__str__())

    total_errors = 0
    for img_name in _ENV.IMG_NAMES:
        boxes = [face.box for face in _scan_faces(img_name)]
        noses = name_2_annotation.get(img_name)

        if noses is not None:
            errors = calculate_errors(boxes, noses)
        else:
            logging.warning(f"[Annotation check] Image '{img_name}' is not annotated, skipping")
            errors = 0

        if errors:
            logging.error(f"[Annotation check] Found '{errors}' error(s) in '{img_name}'")
            total_errors += errors

        if _ENV.SHOW_IMG or _ENV.SHOW_IMG_ON_ERROR and errors:
            img = read_img(IMG_DIR / img_name)
            show_img(img, boxes, noses)

        logging.info(f'Completed: {img_name}')
    if total_errors:
        logging.error(f"[Annotation check] Found a total of {total_errors} error(s)")
        exit(1)
